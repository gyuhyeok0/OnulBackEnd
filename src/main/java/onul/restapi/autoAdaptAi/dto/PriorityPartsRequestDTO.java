package onul.restapi.autoAdaptAi.dto;

import java.util.List;

public class PriorityPartsRequestDTO {
    private String memberId;
    private String priorityParts;

    public PriorityPartsRequestDTO() {
    }

    public PriorityPartsRequestDTO(String memberId, String priorityParts) {
        this.memberId = memberId;
        this.priorityParts = priorityParts;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPriorityParts() {
        return priorityParts;
    }

    public void setPriorityParts(String priorityParts) {
        this.priorityParts = priorityParts;
    }

    @Override
    public String toString() {
        return "PriorityPartsRequestDTO{" +
                "memberId='" + memberId + '\'' +
                ", priorityParts='" + priorityParts + '\'' +
                '}';
    }
}
