package onul.restapi.management.dto;

public class BodyDataRequest {

    private String memberId; // 사용자 ID
    private BodyDataDto bodyData; // bodyData 필드 (중첩된 데이터)

    public BodyDataRequest() {
    }

    public BodyDataRequest(String memberId, BodyDataDto bodyData) {
        this.memberId = memberId;
        this.bodyData = bodyData;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public BodyDataDto getBodyData() {
        return bodyData;
    }

    public void setBodyData(BodyDataDto bodyData) {
        this.bodyData = bodyData;
    }

    @Override
    public String toString() {
        return "BodyDataRequest{" +
                "memberId='" + memberId + '\'' +
                ", bodyData=" + bodyData +
                '}';
    }
}
