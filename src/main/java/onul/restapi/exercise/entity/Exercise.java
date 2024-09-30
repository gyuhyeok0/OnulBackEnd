package onul.restapi.exercise.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_exercises", indexes = {
        @Index(name = "idx_main_muscle_group", columnList = "main_muscle_group")
})
@Getter
@NoArgsConstructor // 기본 생성자를 Lombok으로 처리
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

    // Lombok의 @Builder 어노테이션을 사용해 매개변수가 있는 생성자 대체
    @Builder
    public Exercise(String exerciseName, String mainMuscleGroup, String detailMuscleGroup, Boolean popularityGroup) {
        this.exerciseName = exerciseName;
        this.mainMuscleGroup = mainMuscleGroup;
        this.detailMuscleGroup = detailMuscleGroup;
        this.popularityGroup = popularityGroup;
    }
}
