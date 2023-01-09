package de.haw.vsp.tron.middleware.marshaler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class Marshaler implements IMarshaler {

    @Override
    public String marshal(String methodName, long messageId, Object... args) {
        JSONObject json = new JSONObject();
        JSONArray argsArray = new JSONArray();
        JSONArray argTypesArray = new JSONArray();

        json.put("method_name", methodName);

        for (int i = 0; i < args.length; i++) {
            argsArray.put(args[i]);
            argTypesArray.put(args[i].getClass().getSimpleName().toLowerCase(Locale.ROOT));
        }

        json.put("args", argsArray);
        json.put("arg_types", argTypesArray);
        json.put("msg_id", String.valueOf(messageId));

        return json.toString();
    }


    @Override
    public String marshalReturnValue(long messageId, Object arg) {
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("msg_id", String.valueOf(messageId));
        Class<?> returnType = arg.getClass();

        if (returnType.isArray()) {
            MarshalArray marshalArray = new MarshalArray();
            JSONArray jsonArray = getJSONArray((Object[]) arg, marshalArray);
            marshalArray.createCompleteReturnType((Object[]) arg);

            returnJSON.put("return_type", marshalArray.getReturnType());
            returnJSON.put("return_value", jsonArray);

        } else {
            returnJSON.put("return_type", returnType.toString());
            returnJSON.put("return_value", arg);
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
                marshalArray.setReturnType(object.getClass().getSimpleName());

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
