package onul.restapi.analysis.dto;

import jakarta.persistence.Column;

import java.time.LocalDate;

public class MuscleFatigueDTO {

    @Column(nullable = false)
    private String muscleGroup;

    @Column(nullable = false)
    private double fatigueScore;

    @Column(nullable = false)
    private LocalDate calculationDate;

    public MuscleFatigueDTO() {
    }

    public MuscleFatigueDTO(String muscleGroup, double fatigueScore, LocalDate calculationDate) {
        this.muscleGroup = muscleGroup;
        this.fatigueScore = fatigueScore;
        this.calculationDate = calculationDate;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public double getFatigueScore() {
        return fatigueScore;
    }

    public void setFatigueScore(double fatigueScore) {
        this.fatigueScore = fatigueScore;
    }

    public LocalDate getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(LocalDate calculationDate) {
        this.calculationDate = calculationDate;
    }

    @Override
    public String toString() {
        return "MuscleFatigueDTO{" +
                "muscleGroup='" + muscleGroup + '\'' +
                ", fatigueScore=" + fatigueScore +
                ", calculationDate=" + calculationDate +
                '}';
    }
}
