package onul.restapi.awssns.service;

public class SmsResponse {
    private String state; // 메시지를 제거하고 상태만 남김

    public SmsResponse(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
