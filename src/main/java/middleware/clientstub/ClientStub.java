package middleware.clientstub;

import Enums.TransportType;
import lombok.extern.slf4j.Slf4j;
import middleware.marshaler.INameServerMarshaler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.List;

@Slf4j
@Component
public class ClientStub implements IClientStub{

    String nameServerIp = "192.0.0.1";
    int port = 5549;
    public final static int UDP_PACKET_SIZE = 1024;

    private DatagramSocket udpSocket;
    private Socket tcpSocket;
    private INameServerMarshaler nameServerMarshaler;
    private DatagramPacket udpReceivePacket;

    @Autowired
    public ClientStub(INameServerMarshaler nameServerMarshaler) throws SocketException {
        this.nameServerMarshaler = nameServerMarshaler;
        udpSocket = new DatagramSocket();
    }

    @Override
    public Object invokeSynchronously(String methodName, Class<?> returnType, Object... args) {
        return null;
    }

    @Override
    public void invokeAsynchron(String methodName, TransportType transportType, Object... args) {

    }

    public void initTCPSocket(String host, int port) throws IOException {
        try {
            tcpSocket = new Socket(host, port);
        }catch (IOException excep){
           log.error(String.format("Couldn't connect to a socket with host: %s and port: %d", host, port));
            throw  excep;
        }
    }

    private List<String> lookUp(String methodName) throws UnknownHostException {
        String queryJson = nameServerMarshaler.marshalQueryRequest(methodName);

        byte[] queryJsonBytes = queryJson.getBytes();
        InetAddress ip = InetAddress.getByName(nameServerIp);
        sendUDPPacket(queryJsonBytes, queryJsonBytes.length, ip, port);

        String reponseString = receiveUDPPacket();

        return nameServerMarshaler.unmarshal(reponseString);

    }

    private void sendUDPPacket(byte[] content, int length, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(content, length, ipAddress, port);
        try {
            udpSocket.send(packet);
        } catch (IOException e) {
            log.error(String.format("Packet couldn't be sent to address: %s with port: %d", ipAddress.getHostAddress(), port));
            e.printStackTrace();
        }
    }

    private String receiveUDPPacket(){
        String response = null;
        try {
            byte[] receivedData = new byte[UDP_PACKET_SIZE];
            udpReceivePacket = new DatagramPacket(receivedData, UDP_PACKET_SIZE);
            udpSocket.receive(udpReceivePacket);
            response =  new String(udpReceivePacket.getData(), 0, udpReceivePacket.getLength());

        } catch (Exception ex) {
            System.out.println(ex);
        }

       return response;
    }




}
