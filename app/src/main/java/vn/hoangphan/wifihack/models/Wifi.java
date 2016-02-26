package vn.hoangphan.wifihack.models;

/**
 * Created by ea on 2/26/16.
 */
public class Wifi {
    private String SSID;
    private String password;
    private String capabilities;

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public Wifi(String SSID, String capabilities) {
        this.SSID = SSID;
        this.capabilities = capabilities;
    }
}
