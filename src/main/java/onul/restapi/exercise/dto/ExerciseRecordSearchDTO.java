package onul.restapi.exercise.dto;

import java.time.LocalDate;

public class ExerciseRecordSearchDTO {

    private Long exerciseId;         // Exercise 엔티티와 연결될 ID
    private String memberId;         // Member 엔티티와 연결될 ID
    private int exerciseService;     // Exercise 서비스 ID
    private LocalDate recordDate;    // 조회 날짜

    public ExerciseRecordSearchDTO() {
    }

    public ExerciseRecordSearchDTO(LocalDate recordDate, int exerciseService, String memberId, Long exerciseId) {
        this.recordDate = recordDate;
        this.exerciseService = exerciseService;
        this.memberId = memberId;
        this.exerciseId = exerciseId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getExerciseService() {
        return exerciseService;
    }

    public void setExerciseService(int exerciseService) {
        this.exerciseService = exerciseService;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public String toString() {
        return "ExerciseRecordSearchDTO{" +
                "exerciseId=" + exerciseId +
                ", memberId='" + memberId + '\'' +
                ", exerciseService=" + exerciseService +
                ", recordDate=" + recordDate +
                '}';
    }
}
