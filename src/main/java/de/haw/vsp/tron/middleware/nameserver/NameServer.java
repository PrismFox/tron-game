package de.haw.vsp.tron.middleware.nameserver;

import de.haw.vsp.tron.middleware.middlewareconfig.IMiddlewareConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

@Component
@Slf4j
public class NameServer {

    @Autowired
    private IMiddlewareConfig middlewareConfig;
    Map<String, List<String>> methodIps = new HashMap<>();



    public void start() {
        boolean running = true;
        try (ServerSocket serverSocket = new ServerSocket(middlewareConfig.getNameServerPort())) {
            Socket socket;
            log.info("NameServer started");

            while (running) {
                socket = serverSocket.accept();
                new Thread(new RunnableTCPWorker(socket)).start();
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


    private class RunnableTCPWorker implements Runnable {
        private Socket socket;

        public RunnableTCPWorker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String received = receivePacket();

            InetSocketAddress targetInetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
            InetAddress targetIp = targetInetSocketAddress.getAddress();
            int targetPort = targetInetSocketAddress.getPort();

            JSONObject requestJson = new JSONObject(received);

            String methodType = requestJson.getString("methodType");
            String methodName = requestJson.getString("methodName");

            if ("register".equals(methodType)) {
                List<String> ip = Arrays.asList(targetIp.getHostAddress(), String.valueOf(targetPort));
                register(methodName, ip);
            }
            if ("query".equals(methodType)) {
                query(methodName, targetIp, targetPort);
            }

            try {
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();

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

            String ipReponse = ipResponse.toString();
            sendPacket(ipReponse);
        }

        private String receivePacket() {
            String result = null;
            try {
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                StringBuilder sbLine = new StringBuilder();
                String reply = inputStream.readLine();

                while (!(reply.equals(""))) {
                    sbLine.append(reply).append("\n");
                    reply = inputStream.readLine();
                }
                result = sbLine.toString();
            } catch (IOException exception) {
                log.error("Couldn't read Socket");
            }

            return result;
        }

        public void sendPacket(String message) {
            try {
                byte[] messageBytes = message.getBytes();
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.write(messageBytes);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }

    }
}
