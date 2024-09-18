package onul.restapi.onboarding.dto;

public class OnboardingDTO {
    private String memberId;
    private double height; // 소수점 포함을 위해 double로 변경
    private double weight; // 소수점 포함을 위해 double로 변경
    private int benchPress1rm;
    private int deadlift1rm;
    private int squat1rm;
    private String gender;
    private String heightUnit;
    private String weightUnit;
    private String basicUnit;

    public OnboardingDTO() {
    }

    public OnboardingDTO(String memberId, double height, double weight, int benchPress1rm, int deadlift1rm, int squat1rm, String gender, String heightUnit, String weightUnit, String basicUnit) {
        this.memberId = memberId;
        this.height = height;
        this.weight = weight;
        this.benchPress1rm = benchPress1rm;
        this.deadlift1rm = deadlift1rm;
        this.squat1rm = squat1rm;
        this.gender = gender;
        this.heightUnit = heightUnit;
        this.weightUnit = weightUnit;
        this.basicUnit = basicUnit;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getBenchPress1rm() {
        return benchPress1rm;
    }

    public void setBenchPress1rm(int benchPress1rm) {
        this.benchPress1rm = benchPress1rm;
    }

    public int getDeadlift1rm() {
        return deadlift1rm;
    }

    public void setDeadlift1rm(int deadlift1rm) {
        this.deadlift1rm = deadlift1rm;
    }

    public int getSquat1rm() {
        return squat1rm;
    }

    public void setSquat1rm(int squat1rm) {
        this.squat1rm = squat1rm;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getBasicUnit() {
        return basicUnit;
    }

    public void setBasicUnit(String basicUnit) {
        this.basicUnit = basicUnit;
    }

    @Override
    public String toString() {
        return "OnboardingDTO{" +
                "memberId='" + memberId + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", benchPress1rm=" + benchPress1rm +
                ", deadlift1rm=" + deadlift1rm +
                ", squat1rm=" + squat1rm +
                ", gender='" + gender + '\'' +
                ", heightUnit='" + heightUnit + '\'' +
                ", weightUnit='" + weightUnit + '\'' +
                ", basicUnit='" + basicUnit + '\'' +
                '}';
    }
}
