package de.haw.vsp.tron.middleware.serverstub;

import de.haw.vsp.tron.middleware.applicationstub.IImplCaller;
import de.haw.vsp.tron.middleware.marshaler.IMarshaler;
import de.haw.vsp.tron.middleware.marshaler.INameServerMarshaler;
import de.haw.vsp.tron.middleware.marshaler.IUnmarshaler;
import de.haw.vsp.tron.middleware.middlewareconfig.IMiddlewareConfig;
import de.haw.vsp.tron.middleware.pojo.RequestObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Lazy
    @Autowired
    private IImplCaller implCaller;
    private final IMarshaler marshaler;
    private final IUnmarshaler unmarshaler;
    private final INameServerMarshaler nameServerMarshaler;

    @Autowired
    public ServerStub(IMiddlewareConfig middlewareConfig,
                      IMarshaler marshaler, IUnmarshaler unmarshaler, INameServerMarshaler nameServerMarshaler) {
        this.middlewareConfig = middlewareConfig;
        this.marshaler = marshaler;
        this.unmarshaler = unmarshaler;
        this.nameServerMarshaler = nameServerMarshaler;
        System.out.println("Start of Serverstub");
        new Thread(this::startTCP);
        new Thread(this::startUDP);

    }

    private void startTCP() {
        try (ServerSocket serverSocketTCP = new ServerSocket()) {
            Socket socketTCP;

            while (true) {
                socketTCP = serverSocketTCP.accept();
                new Thread(new RunnableTCPWorker(socketTCP)).start();
            }

        } catch (IOException exception) {
            log.error("Couldn't create Socket");
        }

    }

    private void startUDP() {
        try (DatagramSocket socket = new DatagramSocket()) {
            while (true) {
                byte[] receivedData = new byte[UDP_PACKET_SIZE];
                DatagramPacket udpReceivePacket = new DatagramPacket(receivedData, UDP_PACKET_SIZE);
                socket.receive(udpReceivePacket);
                new Thread(new RunnableUDPWorker(socket, udpReceivePacket));
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void registerMethod(String methodname) {
        new Thread(new NameServerWorker(methodname)).start();
    }


    private class RunnableTCPWorker implements Runnable {
        private Socket socket;

        public RunnableTCPWorker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String request = receivePacket();
            RequestObject requestObject = unmarshaler.unmarshalServerStub(request);

            Object[] objects = requestObject.getArgs();

            for (int i = 0; i < objects.length; i++) {
                if (implCaller.isPrefixedArg(requestObject.getMethodName(), i)) {
                    InetSocketAddress targetInetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
                    String targetIp = targetInetSocketAddress.getAddress().getHostAddress();
                    String targetPort = String.valueOf(targetInetSocketAddress.getPort());

                    String prefixedObject = String.format("%s:%s|%s", targetIp, targetPort, objects[i].toString());
                    objects[i] = prefixedObject;
                }
            }
            Object returnValue = implCaller.callImplementation(requestObject.getMethodName(), objects);

            if (!implCaller.isAsyncMethod(requestObject.getMethodName())) {
                String returnValueStr = marshaler.marshalReturnValue(requestObject.getMessageId(), returnValue);

                sendPacket(returnValueStr);
            }

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
            RequestObject requestObject = unmarshaler.unmarshalServerStub(request);

            String ack = marshaler.marshalReturnValue(requestObject.getMessageId(), "ACK");
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

    private class NameServerWorker implements Runnable {

        private final String methodName;

        public NameServerWorker(String methodName)
        {
            this.methodName = methodName;
        }

        @Override
        public void run() {
            try (Socket socket = new Socket(middlewareConfig.getNameServerIP(), middlewareConfig.getNameServerPort())) {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                String marshaledRequest = nameServerMarshaler.marshalRegisterRequest(methodName);
                outputStream.writeBytes(marshaledRequest);

            } catch (IOException exception) {
                log.error("Error while registering method");
                exception.printStackTrace();
            }
        }
    }
}