package de.haw.vsp.tron.middleware.marshaler;

public interface IMarshaler {
    String marshal(String methodName, long messageId, Object... args);

    String marshalReturnValue(long messageId, Object arg);


}
