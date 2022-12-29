package middleware.clientstub;

import Enums.TransportType;

import java.net.UnknownHostException;

public interface IClientStub {
    
    Object invokeSynchronously(String methodName, Class<?> returnType, Object... args);

    void invokeAsynchron(String methodName, TransportType transportType, Class<?> classType, Object... args) throws UnknownHostException;
}
