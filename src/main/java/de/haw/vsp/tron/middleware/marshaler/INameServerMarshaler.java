package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.NameServerRequestObject;
import de.haw.vsp.tron.middleware.pojo.NameServerResponseObject;

import java.util.List;

public interface INameServerMarshaler {

    String marshalQueryRequest(String methodName);

    String marshalRegisterRequest(String methodName, int port);

    String marshalQueryResponse(List<List<String>> ipLists);

    NameServerRequestObject unmarshalRequest(String methodName);

    List<NameServerResponseObject> unmarshalResponse(String responseJson);

}
