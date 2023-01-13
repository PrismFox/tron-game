package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.NameServerRequestObject;
import de.haw.vsp.tron.middleware.pojo.NameServerResponseObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NameServerMarshaler implements INameServerMarshaler {

    @Override
    public String marshalQueryRequest(String methodName) {
        return this.marshalNameServerRequest("query", methodName);
    }

    @Override
    public String marshalRegisterRequest(String methodName, int port) {
        String registerReq = this.marshalNameServerRequest("register", methodName);
        JSONObject jsonObject = new JSONObject(registerReq);
        return jsonObject.put("port", String.valueOf(port)).toString();
    }


    @Override
    public String marshalQueryResponse(List<List<String>> ipLists) {
        JSONObject ipResponse = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (List<String> ipList : ipLists) {
            JSONObject tempObject = new JSONObject();
            tempObject.put("ip", ipList.get(0));
            tempObject.put("port", ipList.get(1));
            jsonArray.put(tempObject);
        }

        ipResponse.put("providers", jsonArray);

        return ipResponse.toString();
    }

    @Override
    public List<NameServerResponseObject> unmarshalResponse(String responseJson) {
        List<NameServerResponseObject> result = new ArrayList<>();
        JSONObject response = new JSONObject(responseJson);
        JSONArray receivers = response.getJSONArray("providers");

        for (int i = 0; i < receivers.length(); i++) {

            JSONObject ipAndPortObj = receivers.getJSONObject(i);
            String ip = ipAndPortObj.getString("ip");
            String port = ipAndPortObj.getString("port");
            result.add(new NameServerResponseObject(ip, port));

        }
        return result;
    }


    public NameServerRequestObject unmarshalRequest(String message) {
        JSONObject request = new JSONObject(message);

        String methodType = request.getString("method_type");
        String methodName = request.getString("method_name");

        String port;
        if (methodType.equals("register")) {
            port = request.getString("port");
        } else {
            port = "0";
        }


        return new NameServerRequestObject(methodType, methodName, port);
    }


    private String marshalNameServerRequest(String methodType, String methodName) {
        JSONObject request = new JSONObject();
        request.put("method_type", methodType);
        request.put("method_name", methodName);

        return request.toString();
    }


}
