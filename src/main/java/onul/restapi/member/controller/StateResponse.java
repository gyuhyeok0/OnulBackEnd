package onul.restapi.member.controller;

public class StateResponse {
    private String state;

    public StateResponse(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
