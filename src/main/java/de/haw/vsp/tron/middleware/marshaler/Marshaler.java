package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.ResponseObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;

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
    public ResponseObject unmarshal(String message) {
        JSONObject json = new JSONObject(message);
        Object response = null;


        String responseType = json.getString("return_type");
        Constructor<?> responseConstructor = null;
        try {
            Class<?> responseClass = Class.forName(responseType);
            responseConstructor = responseClass.getConstructor();

            String returnValue = json.getString("return_value");
            response = responseConstructor.newInstance(returnValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        long messageId = json.getLong("msg_id");
        return new ResponseObject(messageId, response);
    }
}
