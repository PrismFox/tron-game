package de.haw.vsp.tron.middleware.clientstub;

import de.haw.vsp.tron.Enums.TransportType;
import de.haw.vsp.tron.middleware.marshaler.IMarshaler;
import lombok.extern.slf4j.Slf4j;
import de.haw.vsp.tron.middleware.marshaler.INameServerMarshaler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.List;

@Slf4j
@Component
public class ClientStub implements IClientStub {

    String nameServerIp = "192.0.0.1";
    int port = 5549;
    public static final int UDP_PACKET_SIZE = 1024;

    private DatagramSocket udpSocket;
    private Socket tcpSocket;
    private INameServerMarshaler nameServerMarshaler;
    private DatagramPacket udpReceivePacket;
    private IMarshaler marshaler;

    @Autowired
    public ClientStub(INameServerMarshaler nameServerMarshaler, IMarshaler marshaler) throws SocketException {
        this.nameServerMarshaler = nameServerMarshaler;
        this.marshaler = marshaler;
        udpSocket = new DatagramSocket();
    }

    @Override
    public Object invokeSynchronously(String methodName, Object... args) {
        return null;
    }

    @Override
    public void invokeAsynchron(String methodName, TransportType transportType,
                                Object... args) throws UnknownHostException {
        List<String> address = lookUp(methodName);
        String ip = address.get(0);
        int port = Integer.parseInt(address.get(1));

        String rpcMessage = marshaler.marshal(methodName, "2", args);


        if (transportType.equals(TransportType.TCP)){
            try {
                initTCPSocket(ip, port);
            } catch (IOException e) {
                log.error(String.format("Couldn't send Message to IP: %s and Port: %d  over TCP", address.get(0), port));
            }

        }else {
            byte[] rpcMessageBytes = rpcMessage.getBytes();
            sendUDPPacket(rpcMessageBytes, rpcMessageBytes.length, ip, port);
            String responseMessage = receiveUDPPacket();
        }

    }

    public void initTCPSocket(String host, int port) throws IOException {
        try {
            tcpSocket = new Socket(host, port);
        } catch (IOException excep) {
            log.error(String.format("Couldn't connect to a socket with host: %s and port: %d", host, port));
            throw excep;
        }
    }

    private List<String> lookUp(String methodName){
        String queryJson = nameServerMarshaler.marshalQueryRequest(methodName);

        byte[] queryJsonBytes = queryJson.getBytes();
        sendUDPPacket(queryJsonBytes, queryJsonBytes.length, nameServerIp, port);

        String reponseString = receiveUDPPacket();

        return nameServerMarshaler.unmarshal(reponseString);

    }

    private void sendUDPPacket(byte[] content, int length, String ipAddress, int port) {
        try {
            InetAddress targetAddress = InetAddress.getByName(ipAddress);
            DatagramPacket packet = new DatagramPacket(content, length, targetAddress, port);
            udpSocket.send(packet);
        } catch (IOException e) {
            log.error(String.format("Packet couldn't be sent to address: %s with port: %d", ipAddress, port));
            e.printStackTrace();
        }
    }

    private String receiveUDPPacket() {
        String response = null;
        try {
            byte[] receivedData = new byte[UDP_PACKET_SIZE];
            udpReceivePacket = new DatagramPacket(receivedData, UDP_PACKET_SIZE);
            udpSocket.receive(udpReceivePacket);
            response = new String(udpReceivePacket.getData(), 0, udpReceivePacket.getLength());

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return response;
    }


}
