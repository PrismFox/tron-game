package de.haw.vsp.tron.middleware.marshaler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.UnknownHostException;

public class Marshaler implements IMarshaler{

    @Override
    public String marshal(String methodName, String messageId, Object... args) {
            JSONObject json = new JSONObject();
            JSONArray argsArray = new JSONArray();

            json.put("method_name", methodName);

            for (int i = 0; i < args.length; i++){
                argsArray.put(args[i]);
            }

            json.put("args", argsArray);
            json.put("msg_id", messageId);

            return json.toString();
    }
}
