package onul.restapi.record.dto;

import java.time.LocalDate;

public class MonthDataRequest {

    private LocalDate mountMonth;
    private String memberId;

    public MonthDataRequest() {
    }

    public MonthDataRequest(String memberId, LocalDate mountMonth) {
        this.memberId = memberId;
        this.mountMonth = mountMonth;
    }

    public LocalDate getMountMonth() {
        return mountMonth;
    }

    public void setMountMonth(LocalDate mountMonth) {
        this.mountMonth = mountMonth;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "MonthDataRequest{" +
                "mountMonth=" + mountMonth +
                ", memberId='" + memberId + '\'' +
                '}';
    }


}
