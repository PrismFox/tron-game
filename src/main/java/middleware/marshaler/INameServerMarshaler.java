package middleware.marshaler;

import java.util.List;

public interface INameServerMarshaler {

    String marshalQueryRequest(String methodName);

    String marshalRegisterRequest(String methodName);

    List<String> unmarshal(String responseJson);

}
