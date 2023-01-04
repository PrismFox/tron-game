package de.haw.vsp.tron.middleware.clientstub;

import de.haw.vsp.tron.Enums.TransportType;

import java.net.UnknownHostException;

public interface IClientStub {
    
    Object invokeSynchronously(String methodName, Object... args);

    void invokeAsynchronously(String methodName, TransportType transportType, Object... args) throws UnknownHostException;
}
