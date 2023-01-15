package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.RequestObject;
import de.haw.vsp.tron.middleware.pojo.ResponseObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        System.out.println("unmarshalClientStub: msgID " + messageId + " return value " + returnObject);
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
            String argType = argTypes.getString(i);


            if (argType.contains("<")) { // Map case
                objects[i] = unmarshalMap(args.getJSONObject(i), argType);

            }else if(argType.contains("[]")) {
                //String nonBracketType = argType.replaceFirst("\\[]", "");
                objects[i] = unmarshalArray(argType, args.getJSONArray(i));


            }else {
                objects[i] = unmarshalObject(argTypes.getString(i), args.getString(i));
            }

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
            case "integer":
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
            case "character":
                returnObject = value.charAt(0);
                break;
            case "string":
                returnObject = value;
                break;
            case "boolean":
                returnObject = Boolean.valueOf(value);
                break;
            case "null":
                returnObject = null;
                break;
            default:
                System.err.println("- Default case im unmarshalObject -");
                throw new IllegalArgumentException("unsupported Type " + type + " for object " + value);
        }
        return returnObject;
    }

    private Object[] unmarshalArray(String contentType, JSONArray ary) {
        Object[] resultAry = new Object[ary.length()];
        if (contentType.contains("[]")) {
            System.out.println("{-DEBUG-} Unmarshaler unmarshalArray : contentType before = " + contentType);
            String nextDepthType = contentType.replaceFirst("\\[]", "");
            System.out.println("{-DEBUG-} Unmarshaler unmarshalArray : contentType after = " + nextDepthType);
            for (int i = 0; i < ary.length(); i++) {
                System.out.println("{-DEBUG-} Unmarshaler unmarshalArray : resultAry[i] = " + ary.getJSONArray(i));

                resultAry[i] = unmarshalArray(nextDepthType, ary.getJSONArray(i));
            }
        } else {
            System.out.println("{-DEBUG-} Unmarshaler unmarshalArray : contentType im else = " + contentType);

            for (int i = 0; i < ary.length(); i++) {
                System.out.println("{-DEBUG-} Unmarshaler unmarshalArray : resultAry[i] im else = " + ary.getString(i));
                resultAry[i] = unmarshalObject(contentType, ary.getString(i));
            }
        }
        return resultAry;

    }

    private Map unmarshalMap(JSONObject jsonObject, String type) {
        Map resultMap = new HashMap();
        String identifier = null;

        String subType = type.substring(1, type.length() - 1);
        String[] splitSubType = subType.split(",", 2);


        if (subType.contains("<")) {
            identifier = "Map";

        } else if (subType.contains("[]")) {
            identifier = "List";

        } else {
            identifier = "Primitive";
        }

        for (String key: jsonObject.keySet()){
            Object rightKey = unmarshalObject(splitSubType[0], key);
            Object value = null;

            if (identifier.equals("Map")){
                value = unmarshalMap(jsonObject.getJSONObject(key), splitSubType[1]);
            }
            else if (identifier.equals("List")){
                String nonBracketType = splitSubType[1].replaceFirst("\\[]", "");
                value = unmarshalArray(nonBracketType,jsonObject.getJSONArray(key));
                System.out.println("{-DEBUG-} unmarshalMap: value = " +Arrays.deepToString((Object[]) value));
            }
            else if (identifier.equals("Primitive")){
                value = unmarshalObject(splitSubType[1], jsonObject.getString(key));
            }
            resultMap.put(rightKey, value);
        }

        return resultMap;



    }

}
