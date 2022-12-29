package middleware.marshaler;

import org.springframework.stereotype.Component;

@Component
public class NameServerMarshaler implements INameServerMarshaler{

    @Override
    public String marshalQueryRequest(String methodName) {
        return this.marshalNameServerRequest("query", methodName);
    }

    @Override
    public String marshalRegisterRequest(String methodName) {
        return this.marshalNameServerRequest("register", methodName);
    }

    private String marshalNameServerRequest(String methodType,String methodName){
        return null;
    }


}
