package de.haw.vsp.tron.middleware.nameserver;

import de.haw.vsp.tron.middleware.marshaler.INameServerMarshaler;
import de.haw.vsp.tron.middleware.middlewareconfig.IMiddlewareConfig;
import de.haw.vsp.tron.middleware.pojo.NameServerRequestObject;
import lombok.extern.slf4j.Slf4j;
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

    private IMiddlewareConfig middlewareConfig;

    private INameServerMarshaler marshaler;

    Map<String, List<List<String>>> methodIps = new HashMap<>();


    @Autowired
    public NameServer(IMiddlewareConfig middlewareConfig, INameServerMarshaler marshaler){
        this.middlewareConfig = middlewareConfig;
        this.marshaler = marshaler;
        System.out.println("Start of nameserver");
        start();
    }

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

            NameServerRequestObject nameServerRequestObject = marshaler.unmarshalRequest(received);

            if ("register".equals(nameServerRequestObject.getMethodType())) {
                List<String> ip = Arrays.asList(targetIp.getHostAddress(), nameServerRequestObject.getPort());
                register(nameServerRequestObject.getMethodName(), ip);
            }
            if ("query".equals(nameServerRequestObject.getMethodType())) {
                query(nameServerRequestObject.getMethodName(), targetIp, targetPort);
            }

            try {
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();

            }
        }

        private void register(String methodName, List<String> ip) {
            List<List<String>> value;
            if (!methodIps.containsKey(methodName)) {
                value = new ArrayList<>();

            } else {
                value = methodIps.get(methodName);
            }
            value.add(ip);
            methodIps.put(methodName, value);
        }


        private void query(String methodName, InetAddress ipAddress, int port) {
            List<List<String>> ipLists = methodIps.get(methodName);

            String ipReponse = marshaler.marshalQueryResponse(ipLists);
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
