package de.haw.vsp.tron.middleware.clientstub;

import de.haw.vsp.tron.Enums.TransportType;
import de.haw.vsp.tron.middleware.marshaler.IMarshaler;
import de.haw.vsp.tron.middleware.middlewareconfig.IMiddlewareConfig;
import de.haw.vsp.tron.middleware.pojo.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import de.haw.vsp.tron.middleware.marshaler.INameServerMarshaler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ClientStub implements IClientStub {
    public static final int UDP_PACKET_SIZE = 1024;

    private final INameServerMarshaler nameServerMarshaler;
    private final IMarshaler marshaler;
    private final IMiddlewareConfig middlewareConfig;

    Map<String, List<String>> knownIps = new HashMap<>();

    @Autowired
    public ClientStub(INameServerMarshaler nameServerMarshaler, IMarshaler marshaler, IMiddlewareConfig middlewareConfig) {
        this.nameServerMarshaler = nameServerMarshaler;
        this.marshaler = marshaler;
        this.middlewareConfig = middlewareConfig;
    }

    @Override
    public Object invokeSynchronously(String methodName, Object... args) {
        List<String> address;
        ResponseObject result;

        try (DatagramSocket udpSocket = new DatagramSocket()) {
            if (!knownIps.containsKey(methodName)) {
                address = lookUp(methodName, udpSocket);
                knownIps.put(methodName, address);
            } else {
                address = knownIps.get(methodName);
            }

            String ip = address.get(0);
            int port = Integer.parseInt(address.get(1));

            String messageId = "2";

            String rpcMessage = marshaler.marshal(methodName, messageId, args);
            byte[] rpcMessageBytes = rpcMessage.getBytes();
            result = invokeTCP(ip, port, rpcMessageBytes, messageId, true);
        } catch(SocketException exc) {
            exc.printStackTrace();
            return invokeSynchronously(methodName, args);
        }


        return result;
    }


    @Override
    public void invokeAsynchronously(String methodName, TransportType transportType,
                                     Object... args) {

        List<String> address;
        try (DatagramSocket udpSocket = new DatagramSocket()) {

            if (!knownIps.containsKey(methodName)) {
                address = lookUp(methodName, udpSocket);
                knownIps.put(methodName, address);
            } else {
                address = knownIps.get(methodName);
            }

            String ip = address.get(0);
            int port = Integer.parseInt(address.get(1));

            String messageId = "2";

            String rpcMessage = marshaler.marshal(methodName, messageId, args);
            byte[] rpcMessageBytes = rpcMessage.getBytes();

            if (transportType.equals(TransportType.TCP)) {
                invokeTCP(ip, port, rpcMessageBytes, messageId, false);
            }

            if (transportType.equals(TransportType.UDP)) {
                sendUDPPacket(rpcMessageBytes, rpcMessageBytes.length, ip, port, udpSocket);
            }
        }catch(SocketException exc) {
            exc.printStackTrace();
            invokeAsynchronously(methodName, transportType, args);
        }


    }

    private ResponseObject invokeTCP(String targetIp, int targetPort, byte[] message, String messageId, boolean receive) {
        boolean rightMessageId = false;
        ResponseObject responseObject = null;


        try (Socket socket = initTCPSocket(targetIp, targetPort)) {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            sendTCPPacket(message, outToClient);


            if (receive) {
                while (!rightMessageId) {
                    String responseStr = readResponseTCPPacket(inFromClient);
                    responseObject = marshaler.unmarshalClientStub(responseStr);

                    if (responseObject.getMessageId() == Long.getLong(messageId)) {
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
        } catch (IOException excep) {
            log.error(String.format("Couldn't connect to a socket with host: %s and port: %d", host, port));
        }
        return socket;
    }

    private List<String> lookUp(String methodName, DatagramSocket udpSocket) {
        String queryJson = nameServerMarshaler.marshalQueryRequest(methodName);

        byte[] queryJsonBytes = queryJson.getBytes();
        sendUDPPacket(queryJsonBytes, queryJsonBytes.length,
                middlewareConfig.getNameServerIP(), middlewareConfig.getNameServerPort(), udpSocket);

        String reponseString = receiveUDPPacket(udpSocket);

        return nameServerMarshaler.unmarshal(reponseString);

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
    private void sendTCPPacket(byte[] line, DataOutputStream outToClient) throws IOException {
        /* Sende die Antwortzeile (mit CRLF) zum Client */
        outToClient.write((line));

    }


    private String readResponseTCPPacket(BufferedReader inFromClient) throws IOException {
        /* Lies die naechste Anfrage-Zeile (request) vom Client */
        StringBuilder sbLine = new StringBuilder();
        String reply = inFromClient.readLine();

        while (!(reply.equals(""))) {
            sbLine.append(reply).append("\n");
            reply = inFromClient.readLine();
        }
        return sbLine.toString();
    }


}
