package de.haw.vsp.tron.middleware.marshaler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;


@Component
public class Marshaler implements IMarshaler {

    @Override
    public String marshal(String methodName, long messageId, Object... args) {
        JSONObject json = new JSONObject();
        JSONArray argsArray = new JSONArray();
        JSONArray argTypesArray = new JSONArray();

        json.put("method_name", methodName);

        for (int i = 0; i < args.length; i++) {
            if(!args[i].getClass().isArray()) {
                if(args[i] instanceof Map) {
                    MarshalMap mMap = new MarshalMap((Map) args[i]);
                    argsArray.put(mMap.getJSONObject());
                    argTypesArray.put(mMap.getType());
                } else {
                    argsArray.put(args[i].toString());
                    argTypesArray.put(args[i].getClass().getSimpleName().toLowerCase(Locale.ROOT));
                }
            } else {
                MarshalArray marshalArray = new MarshalArray((Object[])args[i]);
                JSONArray jsonArray = marshalArray.getJSONArray();
                marshalArray.createCompleteReturnType();
                argsArray.put(jsonArray);
                argTypesArray.put(marshalArray.getReturnType());
            }
        }

        json.put("args", argsArray);
        json.put("arg_types", argTypesArray);
        json.put("msg_id", String.valueOf(messageId));

        return json.toString() + "\n";
    }


    @Override
    public String marshalReturnValue(long messageId, Object arg) {
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("msg_id", String.valueOf(messageId));
        if(arg == null){
            returnJSON.put("return_value", "null");
            returnJSON.put("return_type", "null");
            return returnJSON.toString() + "\n";
        }

        Class<?> returnType = arg.getClass();

        if (arg instanceof Map) {
            MarshalMap mMap = new MarshalMap((Map) arg);
            returnJSON.put("return_value", mMap.getJSONObject());
            returnJSON.put("return_type", mMap.getType());
        } else if (returnType.isArray()) {
            MarshalArray marshalArray = new MarshalArray((Object[]) arg);
            JSONArray jsonArray = marshalArray.getJSONArray();
            marshalArray.createCompleteReturnType();

            returnJSON.put("return_type", marshalArray.getReturnType());
            returnJSON.put("return_value", jsonArray);

        } else {
            returnJSON.put("return_type", returnType.toString());
            returnJSON.put("return_value", arg);
        }
        return returnJSON.toString();
    }




    private class MarshalMap {
        private JSONObject mapJSON;
        private String type;
        private String valueType;

        public MarshalMap(Map map) {
            buildJSONMap(map);
            buildTypeString(map);
        }

        public JSONObject getJSONObject() {
            return this.mapJSON;
        }

        private void buildTypeString(Map map) {
            StringBuilder typeSB = new StringBuilder();
            Set keySet = map.keySet();
            typeSB.append("<");
            if(keySet.isEmpty()) {
                typeSB.append("integer"); 
            } else {
                Object key = keySet.iterator().next();
                typeSB.append(key.getClass().getSimpleName().toLowerCase());
            }
            typeSB.append(",");

            Collection values = map.values();
            if(values.isEmpty()) {
                typeSB.append("integer");
            } else {
                typeSB.append(valueType);
            }
            typeSB.append(">");
            type = typeSB.toString();
        }

        public String getType() {
            return this.type;
        }

        private void buildJSONMap(Map map) {
            JSONObject obj = new JSONObject();
            map.entrySet().stream().forEach(e -> {
                Object k = ((Map.Entry) e).getKey();
                Object v = ((Map.Entry) e).getValue();

                if(v.getClass().isArray()) {
                    MarshalArray ary = new MarshalArray((Object[])v);

                    //v = ary.getJSONArray();
                    ary.getJSONArray();

                    if(valueType == null) {
                        ary.createCompleteReturnType();
                        valueType = ary.getReturnType();
                    }
                } else if(v instanceof Map) {
                    MarshalMap mMap = new MarshalMap((Map) v);
                    v = mMap.getJSONObject();
                    if(valueType == null) {
                        valueType = mMap.getType();
                    }
                } else if(valueType == null) {
                    valueType = v.getClass().getSimpleName().toLowerCase();
                }
                
                obj.put(k.toString(), v);
            });
        }
    }

    private class MarshalArray {
        private String returnType;
        private JSONArray jsonArray;

        private Object[] arry;

        public MarshalArray(Object[] arry){
            this.arry = arry;
        }

        public String getReturnType() {
            return returnType;
        }

        public void setReturnType(String returnType) {
            this.returnType = returnType;
        }


        private JSONArray getJSONArray(Object[] objects) {
            JSONArray jsonArray = new JSONArray();

            for (Object object : objects) {
                if (object.getClass().isArray()) {
                    if(object.getClass().getComponentType().isPrimitive()){
                        int arrayLength = Array.getLength(object);
                        Object[] objectArray = new Object[arrayLength];
                        for(int i = 0; i < arrayLength; i++){
                            objectArray[i] = Array.get(object,i);
                        }
                        object = objectArray;
                    }

                    JSONArray furtherDimension = getJSONArray((Object[]) object);
                    jsonArray.put(furtherDimension);
                } else {
                    jsonArray.put(object.toString());
                    this.setReturnType(object.getClass().getSimpleName().toLowerCase());

                }

            }
            return jsonArray;
        }

        public JSONArray getJSONArray() {
            return getJSONArray(arry);
        }

        public void setJsonArray(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        public void addLayers(int count) {
            String bracket = "[]";
            String brackets = bracket.repeat(count);
            this.returnType = returnType + brackets;
        }

        public void createCompleteReturnType() {
            int dimensions = countDimensions(arry);
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
