package onul.restapi.exercise.dto;

import java.util.List;

public class ExerciseVolumeRequest {

    private String memberId;
    private int exerciseServiceNumber;
    private List<Long> exerciseIds;

    public ExerciseVolumeRequest() {
    }

    public ExerciseVolumeRequest(String memberId, int exerciseServiceNumber, List<Long> exerciseIds) {
        this.memberId = memberId;
        this.exerciseServiceNumber = exerciseServiceNumber;
        this.exerciseIds = exerciseIds;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getExerciseServiceNumber() {
        return exerciseServiceNumber;
    }

    public void setExerciseServiceNumber(int exerciseServiceNumber) {
        this.exerciseServiceNumber = exerciseServiceNumber;
    }

    public List<Long> getExerciseIds() {
        return exerciseIds;
    }

    public void setExerciseIds(List<Long> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }

    @Override
    public String toString() {
        return "ExerciseVolumeRequest{" +
                "memberId='" + memberId + '\'' +
                ", exerciseServiceNumber=" + exerciseServiceNumber +
                ", exerciseIds=" + exerciseIds +
                '}';
    }
}
