package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.ResponseObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Component
public class Marshaler implements IMarshaler {

    @Override
    public String marshal(String methodName, String messageId, Object... args) {
        JSONObject json = new JSONObject();
        JSONArray argsArray = new JSONArray();

        json.put("method_name", methodName);

        for (int i = 0; i < args.length; i++) {
            argsArray.put(args[i]);
        }

        json.put("args", argsArray);
        json.put("msg_id", messageId);

        return json.toString();
    }

    @Override
    public ResponseObject unmarshalClientStub(String message) {
        JSONObject json = new JSONObject(message);
        Object returnObject = null;

        String returnType = json.getString("return_type");
        if (!returnType.contains("[]")) {
            String returnValue = json.getString("return_value");
            returnObject = unmarshalObject(returnType, returnValue);
        }else{
        JSONArray returnArray = json.getJSONArray("return_value");
            returnObject = unmarshalArray(returnType.replaceFirst("\\[]", ""), returnArray);
        }

        long messageId = json.getLong("msg_id");
        return new ResponseObject(messageId,returnObject);
    }

    private Object unmarshalObject(String type, String value) {
        Object returnObject = null;
        switch (type) {
            case "byte":
                returnObject = Byte.valueOf(value).byteValue();
                break;
            case "short":
                returnObject = Short.valueOf(value).shortValue();
                break;
            case "int":
                returnObject = Integer.valueOf(value).intValue();
                break;
            case "long":
                returnObject = Long.valueOf(value).longValue();
                break;
            case "float":
                returnObject = Float.valueOf(value).floatValue();
                break;
            case "double":
                returnObject = Double.valueOf(value).doubleValue();
                break;
            case "char":
                returnObject = value.charAt(0);
                break;
            case "string":
                returnObject = value;
                break;
            case "boolean":
                returnObject = Boolean.valueOf(value).booleanValue();
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
