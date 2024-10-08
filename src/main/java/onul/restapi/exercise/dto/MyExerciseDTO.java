package onul.restapi.exercise.dto;

import java.util.List;

public class MyExerciseDTO {

    private String memberId; // 회원 ID
    private List<Long> exerciseIds; // Exercise ID를 포함
    private String muscleGroup; // 필터 정보 (예: 가슴)

    public MyExerciseDTO() {
    }

    public MyExerciseDTO(String memberId, List<Long> exerciseIds, String muscleGroup) {
        this.memberId = memberId;
        this.exerciseIds = exerciseIds;
        this.muscleGroup = muscleGroup;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<Long> getExerciseIds() {
        return exerciseIds;
    }

    public void setExerciseIds(List<Long> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    @Override
    public String toString() {
        return "MyExerciseDTO{" +
                "memberId='" + memberId + '\'' +
                ", exerciseIds=" + exerciseIds +
                ", muscleGroup='" + muscleGroup + '\'' +
                '}';
    }
}
