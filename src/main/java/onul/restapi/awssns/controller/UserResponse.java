package onul.restapi.awssns.controller;

public class UserResponse {
    private boolean exists; // 사용자 존재 여부

    public UserResponse() {
    }

    public UserResponse(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
