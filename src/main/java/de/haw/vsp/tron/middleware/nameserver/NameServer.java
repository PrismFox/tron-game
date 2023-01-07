package de.haw.vsp.tron.middleware.nameserver;

import de.haw.vsp.tron.middleware.middlewareconfig.IMiddlewareConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

@Component
@Slf4j
public class NameServer {

    // Constants
    public static final int UDP_PACKET_SIZE = 1024;

    @Autowired
    private IMiddlewareConfig middlewareConfig;
    private DatagramSocket socket;
    byte[] receivedData;
    Map<String, List<String>> methodIps = new HashMap<>();

    public NameServer() {
        receivedData = new byte[UDP_PACKET_SIZE];
    }


    public void start() {
        boolean running = true;
        try (DatagramSocket socket = new DatagramSocket(middlewareConfig.getNameServerPort())) {
            byte[] receivedData = new byte[UDP_PACKET_SIZE];
            log.info("NameServer started");

            while (running) {
                DatagramPacket udpReceivePacket = new DatagramPacket(receivedData, UDP_PACKET_SIZE);
                socket.receive(udpReceivePacket);
                new Thread(new RunnableUDPWorker(socket, udpReceivePacket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            running = false;

        }
    }


    public static void main(String[] arg) {
        NameServer server = new NameServer();
        server.start();
    }


    private class RunnableUDPWorker implements Runnable {
        private DatagramSocket socket;
        private DatagramPacket udpReceivePacket;

        public RunnableUDPWorker(DatagramSocket socket, DatagramPacket udpReceivePacket) {
            this.socket = socket;
            this.udpReceivePacket = udpReceivePacket;
        }

        @Override
        public void run() {
            InetAddress receivedIPAddress = udpReceivePacket.getAddress();
            int receivedPort = udpReceivePacket.getPort();
            String received = new String(udpReceivePacket.getData(), 0, udpReceivePacket.getLength());

            JSONObject requestJson = new JSONObject(received);

            String methodType = requestJson.getString("methodType");
            String methodName = requestJson.getString("methodName");

            if ("register".equals(methodType)) {
                List<String> ip = Arrays.asList(receivedIPAddress.getHostAddress(), String.valueOf(receivedPort));
                register(methodName, ip);
            }
            if ("query".equals(methodType)) {
                query(methodName, receivedIPAddress, receivedPort);
            }

            socket.close();
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
                log.error(String.format("Packet couldn't be sent to address: %s with port: %d", ipAddress.getHostAddress(), port));
                e.printStackTrace();
            }
        }
    }
}
