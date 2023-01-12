package de.haw.vsp.tron.middleware.pojo;

public class NameServerRequestObject {
    private String methodType;
    private String methodName;

    public NameServerRequestObject(String methodType, String methodName) {
        this.methodType = methodType;
        this.methodName = methodName;
    }

    public String getMethodType() {
        return methodType;
    }

    public String getMethodName() {
        return methodName;
    }
}
