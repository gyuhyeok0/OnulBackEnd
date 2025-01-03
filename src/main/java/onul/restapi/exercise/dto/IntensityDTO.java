package onul.restapi.exercise.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class IntensityDTO {

    private Long id;
    private String memberId;
    private String intensity;
    private Date createdAt;

    public IntensityDTO() {
    }

    public IntensityDTO(Long id, String memberId, String intensity, Date createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.intensity = intensity;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "IntensityDTO{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", intensity='" + intensity + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
