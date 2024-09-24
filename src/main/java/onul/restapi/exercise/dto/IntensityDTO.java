package onul.restapi.exercise.dto;

import java.time.LocalDateTime;

public class IntensityDTO {

    private String memberId;
    private String intensity;

    public IntensityDTO() {
    }

    public IntensityDTO(String memberId, String intensity) {
        this.memberId = memberId;
        this.intensity = intensity;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }
}
