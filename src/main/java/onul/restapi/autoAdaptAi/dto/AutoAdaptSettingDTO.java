package onul.restapi.autoAdaptAi.dto;

import lombok.*;
import onul.restapi.autoAdaptAi.entity.AutoAdaptSettingEntity;

import java.util.List;

public class AutoAdaptSettingDTO {
    private String exerciseGoal;
    private int exerciseSplit;
    private String difficulty;
    private String exerciseTime;
    private List<String> exerciseStyle;
    private List<String> excludedParts;
    private boolean includeCardio;

    public AutoAdaptSettingDTO() {
    }

    public AutoAdaptSettingDTO(String exerciseGoal, int exerciseSplit, String difficulty, String exerciseTime, List<String> exerciseStyle, List<String> excludedParts, boolean includeCardio) {
        this.exerciseGoal = exerciseGoal;
        this.exerciseSplit = exerciseSplit;
        this.difficulty = difficulty;
        this.exerciseTime = exerciseTime;
        this.exerciseStyle = exerciseStyle;
        this.excludedParts = excludedParts;
        this.includeCardio = includeCardio;
    }

    public String getExerciseGoal() {
        return exerciseGoal;
    }

    public void setExerciseGoal(String exerciseGoal) {
        this.exerciseGoal = exerciseGoal;
    }

    public int getExerciseSplit() {
        return exerciseSplit;
    }

    public void setExerciseSplit(int exerciseSplit) {
        this.exerciseSplit = exerciseSplit;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(String exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public List<String> getExerciseStyle() {
        return exerciseStyle;
    }

    public void setExerciseStyle(List<String> exerciseStyle) {
        this.exerciseStyle = exerciseStyle;
    }

    public List<String> getExcludedParts() {
        return excludedParts;
    }

    public void setExcludedParts(List<String> excludedParts) {
        this.excludedParts = excludedParts;
    }

    public boolean isIncludeCardio() {
        return includeCardio;
    }

    public void setIncludeCardio(boolean includeCardio) {
        this.includeCardio = includeCardio;
    }

    @Override
    public String toString() {
        return "AutoAdaptSettingDTO{" +
                "exerciseGoal='" + exerciseGoal + '\'' +
                ", exerciseSplit=" + exerciseSplit +
                ", difficulty='" + difficulty + '\'' +
                ", exerciseTime='" + exerciseTime + '\'' +
                ", exerciseStyle=" + exerciseStyle +
                ", excludedParts=" + excludedParts +
                ", includeCardio=" + includeCardio +
                '}';
    }
}

