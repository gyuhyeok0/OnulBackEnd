package onul.restapi.autoAdaptAi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class AutoAdaptRequestDTO {
    private String memberId;
    private boolean checkDate;
    private boolean initialization;

    public AutoAdaptRequestDTO() {
    }

    public AutoAdaptRequestDTO(String memberId, boolean checkDate, boolean initialization) {
        this.memberId = memberId;
        this.checkDate = checkDate;
        this.initialization = initialization;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public boolean isCheckDate() {
        return checkDate;
    }

    public void setCheckDate(boolean checkDate) {
        this.checkDate = checkDate;
    }

    public boolean isInitialization() {
        return initialization;
    }

    public void setInitialization(boolean initialization) {
        this.initialization = initialization;
    }

    @Override
    public String toString() {
        return "AutoAdaptRequestDTO{" +
                "memberId='" + memberId + '\'' +
                ", checkDate=" + checkDate +
                ", initialization=" + initialization +
                '}';
    }
}
