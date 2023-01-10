package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.RequestObject;
import de.haw.vsp.tron.middleware.pojo.ResponseObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Unmarshaler implements IUnmarshaler {

    @Override
    public ResponseObject unmarshalClientStub(String message) {
        JSONObject json = new JSONObject(message);
        Object returnObject = null;

        String returnType = json.getString("return_type");
        if (!returnType.contains("[]")) {
            String returnValue = json.getString("return_value");
            returnObject = unmarshalObject(returnType, returnValue);
        } else {
            JSONArray returnArray = json.getJSONArray("return_value");
            returnObject = unmarshalArray(returnType.replaceFirst("\\[]", ""), returnArray);
        }

        long messageId = json.getLong("msg_id");
        return new ResponseObject(messageId, returnObject);
    }

    @Override
    public RequestObject unmarshalServerStub(String message) {
        JSONObject json = new JSONObject(message);
        String methodName = json.getString("method_name");
        JSONArray args = json.getJSONArray("args");
        JSONArray argTypes = json.getJSONArray("arg_types");

        Object[] objects = new Object[args.length()];
        for (int i = 0; i < args.length(); i++) {
            objects[i] = unmarshalObject(argTypes.getString(i), args.getString(i));
        }

        long messageId = json.getLong("msg_id");

        return new RequestObject(methodName, objects, messageId);
    }

    private Object unmarshalObject(String type, String value) {
        Object returnObject = null;
        switch (type) {
            case "byte":
                returnObject = Byte.valueOf(value);
                break;
            case "short":
                returnObject = Short.valueOf(value);
                break;
            case "int":
                returnObject = Integer.valueOf(value);
                break;
            case "long":
                returnObject = Long.valueOf(value);
                break;
            case "float":
                returnObject = Float.valueOf(value);
                break;
            case "double":
                returnObject = Double.valueOf(value);
                break;
            case "char":
                returnObject = value.charAt(0);
                break;
            case "string":
                returnObject = value;
                break;
            case "boolean":
                returnObject = Boolean.valueOf(value);
                break;
        }
        return returnObject;
    }

    private Object[] unmarshalArray(String contentType, JSONArray ary) {
        Object[] resultAry = new Object[ary.length()];
        if (contentType.contains("[]")) {
            String nextDepthType = contentType.replaceFirst("\\[]", "");
            for (int i = 0; i < ary.length(); i++) {
                resultAry[i] = unmarshalArray(nextDepthType, ary.getJSONArray(i));
            }
        } else {
            for (int i = 0; i < ary.length(); i++) {
                resultAry[i] = unmarshalObject(contentType, ary.getString(i));
            }
        }
        return resultAry;
    }

}
