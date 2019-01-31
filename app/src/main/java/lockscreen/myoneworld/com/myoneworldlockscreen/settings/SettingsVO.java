package lockscreen.myoneworld.com.myoneworldlockscreen.settings;

public class SettingsVO {
    private int doNotDownload;
    private int wifiOnly;
    private int wifiOrData;

    public int getDoNotDownload() {
        return doNotDownload;
    }

    public void setDoNotDownload(int doNotDownload) {
        this.doNotDownload = doNotDownload;
    }

    public int getWifiOnly() {
        return wifiOnly;
    }

    public void setWifiOnly(int wifiOnly) {
        this.wifiOnly = wifiOnly;
    }

    public int getWifiOrData() {
        return wifiOrData;
    }

    public void setWifiOrData(int wifiOrData) {
        this.wifiOrData = wifiOrData;
    }
}
