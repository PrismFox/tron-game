package de.haw.vsp.tron.middleware.pojo;

public class NameServerRequestObject {
    private String methodType;
    private String methodName;
    private String port;

    public NameServerRequestObject(String methodType, String methodName, String port) {
        this.methodType = methodType;
        this.methodName = methodName;
        this.port = port;
    }

    public String getMethodType() {
        return methodType;
    }

    public String getPort() {
        return port;
    }

    public String getMethodName() {
        return methodName;
    }
}
