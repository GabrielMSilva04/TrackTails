package ies.tracktails.userservice.dtos;

import jakarta.validation.constraints.NotBlank;

public class FindersRequest {
    @NotBlank
    private Long deviceId;

    public FindersRequest() {
    }

    public FindersRequest(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}
