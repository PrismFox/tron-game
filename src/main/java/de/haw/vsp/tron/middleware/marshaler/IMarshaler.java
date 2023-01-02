package de.haw.vsp.tron.middleware.marshaler;

public interface IMarshaler {
    String marshal(String methodName, String messageId,  Object... args);
}
