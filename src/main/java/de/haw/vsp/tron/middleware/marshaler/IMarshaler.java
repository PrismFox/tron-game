package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.ResponseObject;

public interface IMarshaler {
    String marshal(String methodName, String messageId,  Object... args);

    ResponseObject unmarshalClientStub(String message);

    String marshalReturnValue(String messageId, Object... args);
}
