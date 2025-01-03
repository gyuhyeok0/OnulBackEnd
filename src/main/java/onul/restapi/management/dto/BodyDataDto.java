package onul.restapi.management.dto;

import java.time.LocalDate;

public class BodyDataDto {

    private String memberId; // 사용자 ID
    private Double weight; // 체중 (kg)
    private Double weightInLbs; // 체중 (lbs)
    private Double skeletalMuscleMass; // 골격근량 (kg)
    private Double skeletalMuscleMassInLbs; // 골격근량 (lbs)
    private Double bodyFatMass; // 체지방량 (kg)
    private Double bodyFatMassInLbs; // 체지방량 (lbs)
    private Double bodyFatPercentage; // 체지방률 (%)
    private LocalDate date; // 기록 날짜 (YYYY-MM-DD)

    public BodyDataDto() {
    }

    public BodyDataDto(String memberId, Double weight, Double weightInLbs, Double skeletalMuscleMass, Double skeletalMuscleMassInLbs,
                       Double bodyFatMass, Double bodyFatMassInLbs, Double bodyFatPercentage, LocalDate date) {
        this.memberId = memberId;
        this.weight = weight;
        this.weightInLbs = weightInLbs;
        this.skeletalMuscleMass = skeletalMuscleMass;
        this.skeletalMuscleMassInLbs = skeletalMuscleMassInLbs;
        this.bodyFatMass = bodyFatMass;
        this.bodyFatMassInLbs = bodyFatMassInLbs;
        this.bodyFatPercentage = bodyFatPercentage;
        this.date = date;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeightInLbs() {
        return weightInLbs;
    }

    public void setWeightInLbs(Double weightInLbs) {
        this.weightInLbs = weightInLbs;
    }

    public Double getSkeletalMuscleMass() {
        return skeletalMuscleMass;
    }

    public void setSkeletalMuscleMass(Double skeletalMuscleMass) {
        this.skeletalMuscleMass = skeletalMuscleMass;
    }

    public Double getSkeletalMuscleMassInLbs() {
        return skeletalMuscleMassInLbs;
    }

    public void setSkeletalMuscleMassInLbs(Double skeletalMuscleMassInLbs) {
        this.skeletalMuscleMassInLbs = skeletalMuscleMassInLbs;
    }

    public Double getBodyFatMass() {
        return bodyFatMass;
    }

    public void setBodyFatMass(Double bodyFatMass) {
        this.bodyFatMass = bodyFatMass;
    }

    public Double getBodyFatMassInLbs() {
        return bodyFatMassInLbs;
    }

    public void setBodyFatMassInLbs(Double bodyFatMassInLbs) {
        this.bodyFatMassInLbs = bodyFatMassInLbs;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BodyDataDto{" +
                "memberId='" + memberId + '\'' +
                ", weight=" + weight +
                ", weightInLbs=" + weightInLbs +
                ", skeletalMuscleMass=" + skeletalMuscleMass +
                ", skeletalMuscleMassInLbs=" + skeletalMuscleMassInLbs +
                ", bodyFatMass=" + bodyFatMass +
                ", bodyFatMassInLbs=" + bodyFatMassInLbs +
                ", bodyFatPercentage=" + bodyFatPercentage +
                ", date=" + date +
                '}';
    }
}
