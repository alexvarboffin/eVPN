package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;

public class OpenVpnProfile {
    private String remoteHost;
    private int remotePort;
    private String protocol;
    private boolean authUserPass;
    private String caCertificate;
    private String tlsAuthKey;
    private String clientCertificate;
    private String privateKey;

    // Конструкторы, геттеры и сеттеры

    public OpenVpnProfile() {
        // Конструктор по умолчанию
    }

    public OpenVpnProfile(String remoteHost, int remotePort, String protocol, boolean authUserPass,
                          String caCertificate, String tlsAuthKey, String clientCertificate, String privateKey) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.protocol = protocol;
        this.authUserPass = authUserPass;
        this.caCertificate = caCertificate;
        this.tlsAuthKey = tlsAuthKey;
        this.clientCertificate = clientCertificate;
        this.privateKey = privateKey;
    }

    // Геттеры и сеттеры для всех полей

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isAuthUserPass() {
        return authUserPass;
    }

    public void setAuthUserPass(boolean authUserPass) {
        this.authUserPass = authUserPass;
    }

    public String getCaCertificate() {
        return caCertificate;
    }

    public void setCaCertificate(String caCertificate) {
        this.caCertificate = caCertificate;
    }

    public String getTlsAuthKey() {
        return tlsAuthKey;
    }

    public void setTlsAuthKey(String tlsAuthKey) {
        this.tlsAuthKey = tlsAuthKey;
    }

    public String getClientCertificate() {
        return clientCertificate;
    }

    public void setClientCertificate(String clientCertificate) {
        this.clientCertificate = clientCertificate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
