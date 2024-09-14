package onul.restapi.onboarding.dto;

public class OnboardingDTO {

    private Long onboardingId;
    private Long memberId;  // Members 엔티티 대신 memberId만 사용
    private Double height;
    private Double weight;
    private Double benchPress1RM;
    private Double squat1RM;
    private Double deadlift1RM;

    // 기본 생성자
    public OnboardingDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public OnboardingDTO(Long onboardingId, Long memberId, Double height, Double weight, Double benchPress1RM, Double squat1RM, Double deadlift1RM) {
        this.onboardingId = onboardingId;
        this.memberId = memberId;
        this.height = height;
        this.weight = weight;
        this.benchPress1RM = benchPress1RM;
        this.squat1RM = squat1RM;
        this.deadlift1RM = deadlift1RM;
    }

    // Getters and Setters
    public Long getOnboardingId() {
        return onboardingId;
    }

    public void setOnboardingId(Long onboardingId) {
        this.onboardingId = onboardingId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBenchPress1RM() {
        return benchPress1RM;
    }

    public void setBenchPress1RM(Double benchPress1RM) {
        this.benchPress1RM = benchPress1RM;
    }

    public Double getSquat1RM() {
        return squat1RM;
    }

    public void setSquat1RM(Double squat1RM) {
        this.squat1RM = squat1RM;
    }

    public Double getDeadlift1RM() {
        return deadlift1RM;
    }

    public void setDeadlift1RM(Double deadlift1RM) {
        this.deadlift1RM = deadlift1RM;
    }
}
