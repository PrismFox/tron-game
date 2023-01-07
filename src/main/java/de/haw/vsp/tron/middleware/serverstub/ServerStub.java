package de.haw.vsp.tron.middleware.serverstub;

import de.haw.vsp.tron.middleware.applicationstub.IImplCaller;
import de.haw.vsp.tron.middleware.marshaler.IMarshaler;
import de.haw.vsp.tron.middleware.middlewareconfig.IMiddlewareConfig;
import de.haw.vsp.tron.middleware.pojo.RequestObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Slf4j
@Component
public class ServerStub implements IServerStub {

    public static final int UDP_PACKET_SIZE = 1024;

    private final IMiddlewareConfig middlewareConfig;
    private final IImplCaller implCaller;
    private final IMarshaler marshaler;
    private DatagramSocket socketUDP;

    @Autowired
    public ServerStub(IMiddlewareConfig middlewareConfig, IImplCaller implCaller, IMarshaler marshaler) {
        this.middlewareConfig = middlewareConfig;
        this.implCaller = implCaller;
        this.marshaler = marshaler;
        new Thread(this::startTCP);
        new Thread(this::startUDP);
    }

    private void startTCP() {
        try (ServerSocket serverSocketTCP = new ServerSocket()) {
            Socket socketTCP;
            boolean running = true;

            while (running) {
                socketTCP = serverSocketTCP.accept();
                new Thread(new RunnableTCPWorker(socketTCP)).start();
            }

        } catch (IOException exception) {
            log.error("Couldn't create Socket");
        }

    }

    private void startUDP() {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] receivedData = new byte[UDP_PACKET_SIZE];
            DatagramPacket udpReceivePacket = new DatagramPacket(receivedData, UDP_PACKET_SIZE);
            socket.receive(udpReceivePacket);
            new Thread(new RunnableUDPWorker(socket, udpReceivePacket));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }


    public void registerMethod(){

    }


    private class RunnableTCPWorker implements Runnable {
        private Socket socket;

        public RunnableTCPWorker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String request = receivePacket();

            try {
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }

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

        private void sendPacket(String message) {
            try {
                byte[] messageBytes = message.getBytes();
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.write(messageBytes);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }


    }


    private class RunnableUDPWorker implements Runnable {
        private DatagramSocket socket;
        private DatagramPacket requestPacket;

        public RunnableUDPWorker(DatagramSocket socket, DatagramPacket requestPacket) {
            this.socket = socket;
            this.requestPacket = requestPacket;
        }

        @Override
        public void run() {
            String request = processUDPPacket();
            RequestObject requestObject = marshaler.unmarshalServerStub(request);

            String ack = marshaler.marshalReturnValue(String.valueOf(requestObject.getMessageId()), "ACK");
            byte[] ackBytes = ack.getBytes();
            sendUDPACKPacket(ackBytes, ackBytes.length);

        }

        private String processUDPPacket() {
            return new String(requestPacket.getData(), 0, requestPacket.getLength());
        }

        private void sendUDPACKPacket(byte[] content, int length) {
            try {
                InetAddress targetAddress = requestPacket.getAddress();
                DatagramPacket responsePacket = new DatagramPacket(content, length, targetAddress, requestPacket.getPort());
                socket.send(responsePacket);
            } catch (IOException e) {
                log.error(String.format("UDP packet couldn't be sent to address: %s with port: %d",
                        requestPacket.getAddress().getHostAddress(),
                        requestPacket.getPort()));
            }


        }
    }
}