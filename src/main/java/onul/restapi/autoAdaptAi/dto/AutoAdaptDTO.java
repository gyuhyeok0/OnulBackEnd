package onul.restapi.autoAdaptAi.dto;

import java.time.LocalDate;
import java.util.List;

public class AutoAdaptDTO {

    private List<Long> exercises;
    private LocalDate date;
    private String memberId;

    public AutoAdaptDTO() {
    }

    public AutoAdaptDTO(List<Long> exercises, LocalDate date, String memberId) {
        this.exercises = exercises;
        this.date = date;
        this.memberId = memberId;
    }

    public List<Long> getExercises() {
        return exercises;
    }

    public void setExercises(List<Long> exercises) {
        this.exercises = exercises;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "AutoAdaptDTO{" +
                "exercises=" + exercises +
                ", date=" + date +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}
