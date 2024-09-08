package onul.restapi.common;

public class ErrorResponse {
    private String message;

    // 기본 생성자
    public ErrorResponse() {}

    // 메시지를 받는 생성자
    public ErrorResponse(String message) {
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
