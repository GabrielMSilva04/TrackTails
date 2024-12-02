package ies.tracktails.datacollector;

import java.util.Optional;

public class DataDTO {

    private String device_id;
    private Optional<Double> latitude;
    private Optional<Double> longitude;
    private Optional<Double> speed;
    private Optional<Double> bpm;
    private Optional<Double> respiratory_rate;

    public DataDTO() {
        device_id = "";
        latitude = Optional.empty();
        longitude = Optional.empty();
        speed = Optional.empty();
        bpm = Optional.empty();
        respiratory_rate = Optional.empty();
    }

    public DataDTO(String device_id, Optional<Double> latitude, Optional<Double> longitude, Optional<Double> speed, Optional<Double> bpm, Optional<Double> respiratory_rate) {
        this.device_id = device_id;
        this.latitude = Optional.ofNullable(latitude.orElse(null));
        this.longitude = Optional.ofNullable(longitude.orElse(null));
        this.speed = Optional.ofNullable(speed.orElse(null));
        this.bpm = Optional.ofNullable(bpm.orElse(null));
        this.respiratory_rate = Optional.ofNullable(respiratory_rate.orElse(null));
    }


    @Override
    public String toString() {
        return "DataDTO{" +
                "device_id='" + device_id + '\'' +
                ", speed=" + speed +
                ", bpm=" + bpm +
                ", respiratory_rate=" + respiratory_rate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String getDeviceId() {
        return device_id;
    }

    public void setDeviceId(String device_id) {
        this.device_id = device_id;
    }

    public Optional<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(Optional<Double> latitude) {
        this.latitude = latitude;
    }

    public Optional<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(Optional<Double> longitude) {
        this.longitude = longitude;
    }

    public Optional<Double> getSpeed() {
        return speed;
    }

    public void setSpeed(Optional<Double> speed) {
        this.speed = speed;
    }

    public Optional<Double> getBpm() {
        return bpm;
    }

    public void setBpm(Optional<Double> bpm) {
        this.bpm = bpm;
    }

    public Optional<Double> getRespiratory_rate() {
        return respiratory_rate;
    }

    public void setRespiratory_rate(Optional<Double> respiratory_rate) {
        this.respiratory_rate = respiratory_rate;
    }

}
