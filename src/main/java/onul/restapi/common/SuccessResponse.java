package onul.restapi.common;

public class SuccessResponse {

    private String message;

    // 기본 생성자
    public SuccessResponse() {}

    // 메시지를 받는 생성자
    public SuccessResponse(String message) {
        this.message = message;
    }

    // Getter와 Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
