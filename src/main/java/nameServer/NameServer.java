package nameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameServer {

    // Constants
    public final static int SERVER_PORT = 53480;
    public final static int UDP_PACKET_SIZE = 1024;

    private DatagramSocket socket;
    byte[] receivedData;
    Map<String, List<String>> methodIps = new HashMap<>();

    public NameServer() {
        receivedData = new byte[UDP_PACKET_SIZE];
    }

    public void run() throws IOException {
        InetAddress receivedIPAddress;
        int receivedPort;
        DatagramPacket udpReceivePacket;
        boolean running = true;
        socket = new DatagramSocket(SERVER_PORT);

        while (running) {
            try {
                udpReceivePacket = new DatagramPacket(receivedData, UDP_PACKET_SIZE);
                socket.receive(udpReceivePacket);
                receivedIPAddress = udpReceivePacket.getAddress();
                receivedPort = udpReceivePacket.getPort();
                String received = new String(udpReceivePacket.getData(), 0, udpReceivePacket.getLength());

            } catch (IOException e) {
                System.err.println("Error while Receiving");
                running = false;
            }
            socket.close();
        }


    }

    private void registry(String methodName, List<String> parameters) {
    }

    private List<String> query(String methodName, List<String> parameters) {
        List<String> ip = methodIps.get(methodName);
        return null;

    }

    private void sendPacket(byte[] content, int length, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(content, length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.err.println(String.format("Packet couldn't be sent to address: %s with port: %d", ipAddress.getHostAddress(), port));
            e.printStackTrace();
        }
    }

    public static void main(String[] arg) throws Exception {
        NameServer server = new NameServer();
        server.run();
    }
}
