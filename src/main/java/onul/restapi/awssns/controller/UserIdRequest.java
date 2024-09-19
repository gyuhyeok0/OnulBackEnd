package onul.restapi.awssns.controller;

public class UserIdRequest {
    private String userId; // 사용자 아이디 필드

    // 기본 생성자
    public UserIdRequest() {}

    // 사용자 아이디를 설정하는 생성자
    public UserIdRequest(String userId) {
        this.userId = userId;
    }

    // userId 필드의 getter 및 setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
