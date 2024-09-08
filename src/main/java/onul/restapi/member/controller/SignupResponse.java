package onul.restapi.member.controller;

public class SignupResponse {
    private boolean success;
    private String message;

    public SignupResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
