package com.itgangan.shadowsocks.config;

public class Config {
    private String ip;
    private int port;
    private String method;
    private String password;

    public Config(String ip, int port, String method, String password) {
        this.ip = ip;
        this.port = port;
        this.method = method;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getMethod() {
        return method;
    }

    public String getPassword() {
        return password;
    }
}
