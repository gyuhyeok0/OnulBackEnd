package onul.restapi.exercise.dto;

public class ExerciseDto {

    private Long id; // 운동 ID
    private String exerciseName; // 운동 이름
    private String mainMuscleGroup; // 주요 근육 그룹
    private String detailMuscleGroup; // 세부 근육 그룹
    private Boolean popularityGroup; // 인기 그룹 여부
    private Boolean isLiked; // 좋아요 상태

    public ExerciseDto() {
    }

    public ExerciseDto(Long id, String exerciseName, String mainMuscleGroup, String detailMuscleGroup, Boolean popularityGroup, Boolean isLiked) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.mainMuscleGroup = mainMuscleGroup;
        this.detailMuscleGroup = detailMuscleGroup;
        this.popularityGroup = popularityGroup;
        this.isLiked = isLiked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMainMuscleGroup() {
        return mainMuscleGroup;
    }

    public void setMainMuscleGroup(String mainMuscleGroup) {
        this.mainMuscleGroup = mainMuscleGroup;
    }

    public String getDetailMuscleGroup() {
        return detailMuscleGroup;
    }

    public void setDetailMuscleGroup(String detailMuscleGroup) {
        this.detailMuscleGroup = detailMuscleGroup;
    }

    public Boolean getPopularityGroup() {
        return popularityGroup;
    }

    public void setPopularityGroup(Boolean popularityGroup) {
        this.popularityGroup = popularityGroup;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    @Override
    public String toString() {
        return "ExerciseDto{" +
                "id=" + id +
                ", exerciseName='" + exerciseName + '\'' +
                ", mainMuscleGroup='" + mainMuscleGroup + '\'' +
                ", detailMuscleGroup='" + detailMuscleGroup + '\'' +
                ", popularityGroup=" + popularityGroup +
                ", isLiked=" + isLiked +
                '}';
    }
}
