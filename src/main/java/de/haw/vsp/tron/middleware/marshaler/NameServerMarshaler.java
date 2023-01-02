package de.haw.vsp.tron.middleware.marshaler;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<String> unmarshal(String responseJson){
        JSONObject response = new JSONObject(responseJson);
        List<String> address = new ArrayList<>();

        address.add(response.getString("ip"));
        address.add(response.getString("port"));

        return address;
    }

    private String marshalNameServerRequest(String methodType,String methodName){
        JSONObject request = new JSONObject();
        request.put("methodType", methodType);
        request.put("methodName", methodName);

        return request.toString();
    }


}
