package de.haw.vsp.tron.middleware.clientstub;

import de.haw.vsp.tron.Enums.TransportType;
import de.haw.vsp.tron.middleware.marshaler.IMarshaler;
import de.haw.vsp.tron.middleware.marshaler.IUnmarshaler;
import de.haw.vsp.tron.middleware.middlewareconfig.IMiddlewareConfig;
import de.haw.vsp.tron.middleware.pojo.NameServerResponseObject;
import de.haw.vsp.tron.middleware.pojo.ResponseObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import de.haw.vsp.tron.middleware.marshaler.INameServerMarshaler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.util.*;

@Slf4j
@Component
public class ClientStub implements IClientStub {
    public static final int UDP_PACKET_SIZE = 1024;
    public static final int TIMEOUT = 1000000;

    private final INameServerMarshaler nameServerMarshaler;
    private final IMarshaler marshaler;
    private final IUnmarshaler unmarshaler;
    private final IMiddlewareConfig middlewareConfig;
    private static long messageIdInc = 1;

    Map<String, List<List<String>>> knownIps = new HashMap<>();

   @Autowired
    public ClientStub(INameServerMarshaler nameServerMarshaler, IMarshaler marshaler, IUnmarshaler unmarshaler, IMiddlewareConfig middlewareConfig){
        this.nameServerMarshaler = nameServerMarshaler;
        this.marshaler = marshaler;
        this.unmarshaler = unmarshaler;
        this.middlewareConfig = middlewareConfig;
    }

    @Override
    public Object invokeSynchronously(String methodName, Object... args) {
        List<List<String>> addresses;
        ResponseObject result = null;

        try {
            if (!knownIps.containsKey(methodName)) {
                log.info("Lookup");
                addresses = lookUp(methodName);
                log.info("lookup done");
                knownIps.put(methodName, addresses);
            } else {
                addresses = knownIps.get(methodName);
            }

            for (List<String> address : addresses) {
                String ip = address.get(0);
                int port = Integer.parseInt(address.get(1));

                long messageId = messageIdInc++;

                String rpcMessage = marshaler.marshal(methodName, messageId, args);
                byte[] rpcMessageBytes = rpcMessage.getBytes();

                result = invokeTCP(ip, port, rpcMessageBytes, String.valueOf(messageId), true);
            }

        } catch (SocketException exc) {
            exc.printStackTrace();
            return invokeSynchronously(methodName, args);
        } catch (IOException exception) {
            exception.printStackTrace();
            return invokeSynchronously(methodName, args);
        }
        if(result == null){
            return null;
        }
        return result.getReturnValue();
    }


    @Override
    public void invokeAsynchronously(String methodName, TransportType transportType,
                                     Object... args) {
        List<List<String>> addresses;
        try (DatagramSocket udpSocket = new DatagramSocket()) {

            if (!knownIps.containsKey(methodName)) {
                addresses = lookUp(methodName);
                knownIps.put(methodName, addresses);
            } else {
                addresses = knownIps.get(methodName);
            }

            for (List<String> address : addresses) {
                String ip = address.get(0);
                int port = Integer.parseInt(address.get(1));

                long messageId = messageIdInc++;

                String rpcMessage = marshaler.marshal(methodName, messageId, args);
                byte[] rpcMessageBytes = rpcMessage.getBytes();

                if (transportType.equals(TransportType.TCP)) {
                    invokeTCP(ip, port, rpcMessageBytes, String.valueOf(messageId), false);
                }

                if (transportType.equals(TransportType.UDP)) {
                    sendUDPPacket(rpcMessageBytes, rpcMessageBytes.length, ip, port, udpSocket);
                }
            }

        } catch (SocketException exc) {
            exc.printStackTrace();
            invokeAsynchronously(methodName, transportType, args);
        } catch (IOException exception) {
            exception.printStackTrace();
            invokeAsynchronously(methodName, transportType, args);
        }

    }

    private ResponseObject invokeTCP(String targetIp, int targetPort, byte[] message, String messageId, boolean receive) {
        boolean rightMessageId = false;
        ResponseObject responseObject = null;


        try (Socket socket = initTCPSocket(targetIp, targetPort)) {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            log.info("IpAddress: {} with message: {}", socket.getLocalAddress(), message);
            sendTCPPacket(message, outputStream);


            if (receive) {
                while (!rightMessageId) { //schleife kann drin bleiben fuer performance
                    String responseStr = receiveResponseTCPPacket(inputStream);
                    responseObject = unmarshaler.unmarshalClientStub(responseStr);

                    if (responseObject.getMessageId() == Long.parseLong(messageId)) {
                        rightMessageId = true;
                    }

                }
            }


        } catch (IOException exception) {
            log.error("Error while trying to send TCP packet");
        }

        return responseObject;
    }

    public Socket initTCPSocket(String host, int port) {
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(TIMEOUT);
        } catch (IOException excep) {
            log.error(String.format("Couldn't connect to a socket with host: %s and port: %d", host, port));
            excep.printStackTrace();
        }
        return socket;
    }

    private List<List<String>> lookUp(String methodName) throws IOException {
        String responseString;
        String queryJson = nameServerMarshaler.marshalQueryRequest(methodName);
        List<List<String>> addresses = new ArrayList<>();

        try (Socket socket = initTCPSocket(middlewareConfig.getNameServerIP(), middlewareConfig.getNameServerPort())) {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            sendTCPPacket(queryJson.getBytes(), outputStream);
            responseString = receiveResponseTCPPacket(inputStream);
        }
        List<NameServerResponseObject> responsePojo = nameServerMarshaler.unmarshalResponse(responseString);

        responsePojo.forEach(pojo -> addresses.add(new ArrayList<>(Arrays.asList(pojo.getIp(), pojo.getPort()))));

        return addresses;

        // sendUDPPacket(queryJsonBytes, queryJsonBytes.length, middlewareConfig.getNameServerIP(), middlewareConfig.getNameServerPort(), udpSocket);
        //String reponseString = receiveUDPPacket(udpSocket);
    }

    private void sendUDPPacket(byte[] content, int length, String ipAddress, int port, DatagramSocket udpSocket) {
        try {
            InetAddress targetAddress = InetAddress.getByName(ipAddress);
            DatagramPacket packet = new DatagramPacket(content, length, targetAddress, port);
            udpSocket.send(packet);
        } catch (IOException e) {
            log.error(String.format("UDP packet couldn't be sent to address: %s with port: %d", ipAddress, port));
        }
    }

    private String receiveUDPPacket(DatagramSocket udpSocket) {
        String response = null;
        try {
            byte[] receivedData = new byte[UDP_PACKET_SIZE];
            DatagramPacket udpReceivePacket = new DatagramPacket(receivedData, UDP_PACKET_SIZE);
            udpSocket.receive(udpReceivePacket);
            response = new String(udpReceivePacket.getData(), 0, udpReceivePacket.getLength());

        } catch (IOException ex) {
            log.error("Error while receiving UDP packet");
        }

        return response;
    }

    //Schreibt antworten an den Client
    private void sendTCPPacket(byte[] line, DataOutputStream outputStream) throws IOException {
        /* Sende die Antwortzeile (mit CRLF) zum Client */
        outputStream.write((line));

    }


    private String receiveResponseTCPPacket(BufferedReader inputStream) throws IOException {
        String reply = null;
        try {
            log.info("ClientStub: Start of reading packet");

            reply = inputStream.readLine();
            log.info("ClientStub: Done with reading packet. Buffer result: " + reply);
        } catch (IOException exception) {
            log.error("Couldn't read Socket");
            exception.printStackTrace();
        }

        return reply;
    }


}
