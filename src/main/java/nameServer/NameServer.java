package nameServer;

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

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

                JSONObject requestJson = new JSONObject(received);

                String methodType = requestJson.getString("methodType");
                String methodName = requestJson.getString("methodName");

                if ("register".equals(methodType)) {
                    List<String> ip = Arrays.asList(receivedIPAddress.toString(), String.valueOf(receivedPort));
                    register(methodName, ip);
                }
                if ("query".equals(methodType)) {
                    query(methodName, receivedIPAddress, receivedPort);
                }

            } catch (IOException e) {
                System.err.println("Error while Receiving");
                running = false;
            }
            socket.close();
        }
    }

    private void register(String methodName, List<String> ip) {
        methodIps.put(methodName, ip);
    }

    private void query(String methodName, InetAddress ipAddress, int port) {
        List<String> ip = methodIps.get(methodName);

        JSONObject ipResponse = new JSONObject();
        ipResponse.put("ip", ip.get(0));
        ipResponse.put("port", ip.get(1));

        byte[] ipReponseBytes = ipResponse.toString().getBytes();
        sendPacket(ipReponseBytes, ipReponseBytes.length, ipAddress, port);
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
