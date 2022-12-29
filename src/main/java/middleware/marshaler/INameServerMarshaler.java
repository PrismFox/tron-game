package middleware.marshaler;

public interface INameServerMarshaler {

    String marshalQueryRequest(String methodName);

    String marshalRegisterRequest(String methodName);

}
