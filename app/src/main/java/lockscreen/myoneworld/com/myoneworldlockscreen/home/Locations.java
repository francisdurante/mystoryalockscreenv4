package lockscreen.myoneworld.com.myoneworldlockscreen.home;

public class Locations {
    private String location;
    private String longitude;
    private String latitude;
    private String province;

    public Locations(String location, String lon, String lat,String province) {
        this.location = location;
        this.longitude = lon;
        this.latitude = lat;
        this.province = province;
    }
}
