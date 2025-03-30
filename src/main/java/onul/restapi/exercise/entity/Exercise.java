package onul.restapi.exercise.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_exercises", indexes = {
        @Index(name = "idx_main_detail_muscle_group", columnList = "main_muscle_group, detail_muscle_group"),
        @Index(name = "idx_exercise_name", columnList = "exercise_name")
})
@Getter
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @Column(name = "main_muscle_group", nullable = false)
    private String mainMuscleGroup;

    @Column(name = "detail_muscle_group")
    private String detailMuscleGroup;

    @Column(name = "popularity_group", nullable = false)
    private Boolean popularityGroup;

    @Column(name = "is_liked", nullable = false)
    private Boolean isLiked = false; // 기본값 false

    @Column(name = "exercise_type") // 운동 종류를 나타내는 필드 추가
    private String exerciseType;

    public Exercise() {
    }

    public Exercise(Long id, String exerciseName, String mainMuscleGroup, String detailMuscleGroup, Boolean popularityGroup, Boolean isLiked, String exerciseType) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.mainMuscleGroup = mainMuscleGroup;
        this.detailMuscleGroup = detailMuscleGroup;
        this.popularityGroup = popularityGroup;
        this.isLiked = isLiked;
        this.exerciseType = exerciseType;
    }


    // id를 받는 생성자 추가
    public Exercise(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", exerciseName='" + exerciseName + '\'' +
                ", mainMuscleGroup='" + mainMuscleGroup + '\'' +
                ", detailMuscleGroup='" + detailMuscleGroup + '\'' +
                ", popularityGroup=" + popularityGroup +
                ", isLiked=" + isLiked +
                ", exerciseType='" + exerciseType + '\'' +
                '}';
    }
}
