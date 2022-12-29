package middleware.clientstub;

import Enums.TransportType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

@Slf4j
@Component
public class ClientStub implements IClientStub{

    String nameServerIp = "192.0.0.1";
    int port = 5549;

    private DatagramSocket udpSocket;
    private Socket tcpSocket;

    public ClientStub() throws SocketException {
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

    private List<String> lookUp(){
        return null;
    }




}
