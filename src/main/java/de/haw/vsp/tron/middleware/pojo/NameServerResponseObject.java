package de.haw.vsp.tron.middleware.pojo;

public class NameServerResponseObject {
    private String ip;
    private String port;

    public NameServerResponseObject(String ip, String port){
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
}
