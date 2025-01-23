package onul.restapi.management.dto;

import java.time.LocalDate;

public class RecordRequest {

    private String memberId;
    private LocalDate date;

    public RecordRequest() {
    }

    public RecordRequest(String memberId, LocalDate date) {
        this.memberId = memberId;
        this.date = date;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RecordRequest{" +
                "memberId='" + memberId + '\'' +
                ", date=" + date +
                '}';
    }
}
