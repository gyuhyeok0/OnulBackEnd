package onul.restapi.exercise.dto;

public class RecordDateRequest {

    private String memberId;
    private Long exerciseId;
    private Integer exerciseService;

    public RecordDateRequest() {
    }

    public RecordDateRequest(Integer exerciseService, Long exerciseId, String memberId) {
        this.exerciseService = exerciseService;
        this.exerciseId = exerciseId;
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getExerciseService() {
        return exerciseService;
    }

    public void setExerciseService(Integer exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Override
    public String toString() {
        return "RecordDateRequest{" +
                "memberId='" + memberId + '\'' +
                ", exerciseId=" + exerciseId +
                ", exerciseService=" + exerciseService +
                '}';
    }
}
