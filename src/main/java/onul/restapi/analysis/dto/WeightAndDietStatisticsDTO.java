package onul.restapi.analysis.dto;

import java.time.LocalDate;

public class WeightAndDietStatisticsDTO {

    private LocalDate date;
    private Double averageWeight;
    private Double averageBodyFatMass;
    private Double averageSkeletalMuscleMass;
    private Double averageCalories;
    private Double averageProtein;
    private Double averageCarbohydrates;

    public WeightAndDietStatisticsDTO() {
    }

    public WeightAndDietStatisticsDTO(LocalDate date, Double averageWeight, Double averageBodyFatMass, Double averageSkeletalMuscleMass, Double averageCalories, Double averageProtein, Double averageCarbohydrates) {
        this.date = date;
        this.averageWeight = averageWeight;
        this.averageBodyFatMass = averageBodyFatMass;
        this.averageSkeletalMuscleMass = averageSkeletalMuscleMass;
        this.averageCalories = averageCalories;
        this.averageProtein = averageProtein;
        this.averageCarbohydrates = averageCarbohydrates;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAverageWeight() {
        return averageWeight;
    }

    public void setAverageWeight(Double averageWeight) {
        this.averageWeight = averageWeight;
    }

    public Double getAverageBodyFatMass() {
        return averageBodyFatMass;
    }

    public void setAverageBodyFatMass(Double averageBodyFatMass) {
        this.averageBodyFatMass = averageBodyFatMass;
    }


    public Double getAverageSkeletalMuscleMass() {
        return averageSkeletalMuscleMass;
    }

    public void setAverageSkeletalMuscleMass(Double averageSkeletalMuscleMass) {
        this.averageSkeletalMuscleMass = averageSkeletalMuscleMass;
    }

    public Double getAverageCalories() {
        return averageCalories;
    }

    public void setAverageCalories(Double averageCalories) {
        this.averageCalories = averageCalories;
    }

    public Double getAverageProtein() {
        return averageProtein;
    }

    public void setAverageProtein(Double averageProtein) {
        this.averageProtein = averageProtein;
    }

    public Double getAverageCarbohydrates() {
        return averageCarbohydrates;
    }

    public void setAverageCarbohydrates(Double averageCarbohydrates) {
        this.averageCarbohydrates = averageCarbohydrates;
    }

    @Override
    public String toString() {
        return "WeightAndDietStatisticsDTO{" +
                "date=" + date +
                ", averageWeight=" + averageWeight +
                ", averageBodyFatMass=" + averageBodyFatMass +
                ", averageSkeletalMuscleMass=" + averageSkeletalMuscleMass +
                ", averageCalories=" + averageCalories +
                ", averageProtein=" + averageProtein +
                ", averageCarbohydrates=" + averageCarbohydrates +
                '}';
    }
}
