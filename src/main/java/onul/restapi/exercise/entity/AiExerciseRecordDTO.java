package onul.restapi.exercise.entity;

import onul.restapi.exercise.dto.ExerciseDto;

import java.time.LocalDate;

public class AiExerciseRecordDTO {

    private ExerciseDto exercise;
    private LocalDate recordDate;

    public AiExerciseRecordDTO() {
    }

    public AiExerciseRecordDTO(ExerciseDto exercise, LocalDate recordDate) {
        this.exercise = exercise;
        this.recordDate = recordDate;
    }

    public ExerciseDto getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDto exercise) {
        this.exercise = exercise;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public String toString() {
        return "AiExerciseRecordDTO{" +
                "exercise=" + exercise +
                ", recordDate=" + recordDate +
                '}';
    }
}
