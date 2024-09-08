package onul.restapi.awssns.service;

public class SmsResponse {

    private String status;

    // 기본 생성자
    public SmsResponse() {}

    // 생성자
    public SmsResponse(String status) {
        this.status = status;
    }

    // Getter와 Setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
