package ies.tracktails.datacollector;

public class DataDTO {

    private String deviceId;
    private double speed;
    private double bpm;
    private double respiratory_rate;
    private double latitude;
    private double longitude;

    public DataDTO() {
    }

    public DataDTO(String deviceId, double speed, double bpm, double respiratory_rate, double latitude, double longitude) {
        this.deviceId = deviceId;
        this.speed = speed;
        this.bpm = bpm;
        this.respiratory_rate = respiratory_rate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public double getRespiratory_rate() {
        return respiratory_rate;
    }

    public void setRespiratory_rate(double respiratory_rate) {
        this.respiratory_rate = respiratory_rate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "DataDTO{" +
                "deviceId='" + deviceId + '\'' +
                ", speed=" + speed +
                ", bpm=" + bpm +
                ", respiratory_rate=" + respiratory_rate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
