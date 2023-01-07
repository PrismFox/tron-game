package de.haw.vsp.tron.middleware.pojo;

public class RequestObject {
    private String methodName;
    private Object[] args;
    private long messageId;

    public RequestObject(String methodName, Object[] args, long messageId){
        this.methodName = methodName;
        this.args = args;
        this.messageId = messageId;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public long getMessageId() {
        return messageId;
    }
}
