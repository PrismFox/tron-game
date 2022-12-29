package middleware.clientstub;

import Enums.TransportType;

public interface IClientStub {
    
    Object invokeSynchronously(String methodName, Class<?> returnType, Object... args);

    void invokeAsynchron(String methodName, TransportType transportType, Object... args);
}
