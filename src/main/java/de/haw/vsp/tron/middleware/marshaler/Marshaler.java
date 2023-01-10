package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.ResponseObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;


@Component
public class Marshaler implements IMarshaler {

    @Override
    public String marshal(String methodName, String messageId, Object... args) {
        JSONObject json = new JSONObject();
        JSONArray argsArray = new JSONArray();
        JSONArray argTypes = new JSONArray();

        json.put("method_name", methodName);

        for (int i = 0; i < args.length; i++) {
            argsArray.put(args[i]);
            argTypes.put(args[i].getClass().getSimpleName().toLowerCase());
        }

        json.put("args", argsArray);
        json.put("msg_id", messageId);
        json.put("arg_types", argTypes);

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
        } else {
            JSONArray returnArray = json.getJSONArray("return_value");
            returnObject = unmarshalArray(returnType.replaceFirst("\\[]", ""), returnArray);
        }

        long messageId = json.getLong("msg_id");
        return new ResponseObject(messageId, returnObject);
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

    @Override
    public String marshalReturnValue(String messageId, Object... args) {
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("msg_id", messageId);
        Class<?> returnType = args[0].getClass();

        if (args.length > 1) {
            MarshalArray marshalArray = new MarshalArray();
            JSONArray jsonArray = getJSONArray(args, marshalArray);
            marshalArray.createCompleteReturnType(args);

            returnJSON.put("return_type", marshalArray.getReturnType());
            returnJSON.put("return_value", jsonArray);

        } else {
            returnJSON.put("return_type", returnType.toString());
            returnJSON.put("return_value", args[0]);
        }
        return returnJSON.toString();
    }

    private JSONArray getJSONArray(Object[] objects, MarshalArray marshalArray) {
        JSONArray jsonArray = new JSONArray();

        for (Object object : objects) {
            if (object.getClass().isArray()) {
                JSONArray furtherDimension = getJSONArray(objects, marshalArray);
                jsonArray.put(furtherDimension);
            } else {
                jsonArray.put(object);
                marshalArray.setReturnType(object.getClass().getSimpleName().toLowerCase());

            }

        }
        return jsonArray;
    }


    private class MarshalArray {
        private String returnType;
        private JSONArray jsonArray;


        public String getReturnType() {
            return returnType;
        }

        public void setReturnType(String returnType) {
            this.returnType = returnType;
        }

        public JSONArray getJsonArray() {
            return jsonArray;
        }

        public void setJsonArray(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        public void addLayers(int count) {
            String bracket = "[]";
            String brackets = bracket.repeat(count);
            this.returnType = returnType + brackets;
        }

        public void createCompleteReturnType(Object[] arr) {
            int dimensions = countDimensions(arr);
            addLayers(dimensions);
        }


        private int countDimensions(Object[] arr) {
            int dimensionCount = 0;
            Class<?> c = arr.getClass(); // getting the runtime class of an object
            while (c.isArray()) { // check whether the object is an array
                c = c.getComponentType(); // returns the class denoting the component type of the array
                dimensionCount++;
            }
            return dimensionCount;
        }
    }
}
