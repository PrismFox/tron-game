package de.haw.vsp.tron.middleware.clientstub;

import de.haw.vsp.tron.Enums.TransportType;
import de.haw.vsp.tron.middleware.marshaler.IMarshaler;
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
import java.util.List;

@Slf4j
@Component
public class ClientStub implements IClientStub {

    String nameServerIp = "192.0.0.1";
    int portNameServer = 5549;
    public static final int UDP_PACKET_SIZE = 1024;

    private DatagramSocket udpSocket;
    private Socket tcpSocket;
    private INameServerMarshaler nameServerMarshaler;
    private DatagramPacket udpReceivePacket;
    private IMarshaler marshaler;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;

    @Autowired
    public ClientStub(INameServerMarshaler nameServerMarshaler, IMarshaler marshaler) throws SocketException {
        this.nameServerMarshaler = nameServerMarshaler;
        this.marshaler = marshaler;
        udpSocket = new DatagramSocket();
    }

    @Override
    public Object invokeSynchronously(String methodName, Object... args) {
        List<String> address = lookUp(methodName);
        String ip = address.get(0);
        int port = Integer.parseInt(address.get(1));

        String messageId = "2";

        String rpcMessage = marshaler.marshal(methodName, messageId, args);
        byte[] rpcMessageBytes = rpcMessage.getBytes();

        return invokeTCP(ip, port, rpcMessageBytes, messageId, true);
    }


    @Override
    public void invokeAsynchronously(String methodName, TransportType transportType,
                                     Object... args) {

        List<String> address = lookUp(methodName);
        String ip = address.get(0);
        int port = Integer.parseInt(address.get(1));

        String messageId = "2";

        String rpcMessage = marshaler.marshal(methodName, messageId, args);
        byte[] rpcMessageBytes = rpcMessage.getBytes();

        if (transportType.equals(TransportType.TCP)) {
            invokeTCP(ip, port, rpcMessageBytes, messageId, false);
        }

        if (transportType.equals(TransportType.UDP)) {
            sendUDPPacket(rpcMessageBytes, rpcMessageBytes.length, ip, port);
        }

    }

    private ResponseObject invokeTCP(String targetIp, int targetPort, byte[] message, String messageId, boolean receive) {
        boolean rightMessageId = false;

        initTCPSocket(targetIp, targetPort);

        try {
            inFromClient = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            outToClient = new DataOutputStream(tcpSocket.getOutputStream());
            sendTCPPacket(message);
        } catch (IOException exception) {
            log.error("Error while trying to send TCP packet");
        }


        ResponseObject responseObject = null;

        if (receive) {
            while (!rightMessageId) {
                String responseStr = receiveTCPPacket();
                responseObject = marshaler.unmarshalClientStub(responseStr);

                if (responseObject.getMessageId() == Long.getLong(messageId)) {
                    rightMessageId = true;
                }

            }
        }

        return responseObject;
    }

    public void initTCPSocket(String host, int port) {
        try {
            tcpSocket = new Socket(host, port);
        } catch (IOException excep) {
            log.error(String.format("Couldn't connect to a socket with host: %s and port: %d", host, port));
        }
    }

    private List<String> lookUp(String methodName) {
        String queryJson = nameServerMarshaler.marshalQueryRequest(methodName);

        byte[] queryJsonBytes = queryJson.getBytes();
        sendUDPPacket(queryJsonBytes, queryJsonBytes.length, nameServerIp, portNameServer);

        String reponseString = receiveUDPPacket();

        return nameServerMarshaler.unmarshal(reponseString);

    }

    private void sendUDPPacket(byte[] content, int length, String ipAddress, int port) {
        try {
            InetAddress targetAddress = InetAddress.getByName(ipAddress);
            DatagramPacket packet = new DatagramPacket(content, length, targetAddress, port);
            udpSocket.send(packet);
        } catch (IOException e) {
            log.error(String.format("UDP packet couldn't be sent to address: %s with port: %d", ipAddress, port));
        }
    }

    private String receiveUDPPacket() {
        String response = null;
        try {
            byte[] receivedData = new byte[UDP_PACKET_SIZE];
            udpReceivePacket = new DatagramPacket(receivedData, UDP_PACKET_SIZE);
            udpSocket.receive(udpReceivePacket);
            response = new String(udpReceivePacket.getData(), 0, udpReceivePacket.getLength());

        } catch (IOException ex) {
            log.error("Error while receiving UDP packet");
        }

        return response;
    }

    //Schreibt antworten an den Client
    private void sendTCPPacket(byte[] line) throws IOException {
        /* Sende die Antwortzeile (mit CRLF) zum Client */
        outToClient.write((line));

    }

    private String receiveTCPPacket() {
        String responseStr = null;
        try {
            responseStr = readResponseTCPPacket();

        } catch (IOException e) {
            log.error("Error while receiving TCP packet");
        }

        return responseStr;
    }

    private String readResponseTCPPacket() throws IOException {
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
