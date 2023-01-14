package de.haw.vsp.tron.middleware.pojo;

import lombok.Data;

@Data
public class ResponseObject {
    private long messageId;
    private Object returnValue;

    public ResponseObject(long messageId, Object returnValue)
    {
        this.messageId = messageId;
        this.returnValue = returnValue;
    }

    public long getMessageId() {
        return messageId;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
