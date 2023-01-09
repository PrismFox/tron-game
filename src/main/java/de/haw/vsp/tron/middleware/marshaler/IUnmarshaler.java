package de.haw.vsp.tron.middleware.marshaler;

import de.haw.vsp.tron.middleware.pojo.RequestObject;
import de.haw.vsp.tron.middleware.pojo.ResponseObject;

public interface IUnmarshaler {
    ResponseObject unmarshalClientStub(String message);

    RequestObject unmarshalServerStub(String message);
}
