package ies.tracktails.datacollector;

public class DataDTO {
    private long timestamp;
    private String deviceId;
    private String state;
    private double speed;
    private double bpm;
    private double latitude;
    private double longitude;

    public DataDTO() {
    }

    public DataDTO(long timestamp, String deviceId, String state, double speed, double bpm, double latitude, double longitude) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.state = state;
        this.speed = speed;
        this.bpm = bpm;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
                "timestamp=" + timestamp +
                ", deviceId='" + deviceId + '\'' +
                ", state='" + state + '\'' +
                ", speed=" + speed +
                ", bpm=" + bpm +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
