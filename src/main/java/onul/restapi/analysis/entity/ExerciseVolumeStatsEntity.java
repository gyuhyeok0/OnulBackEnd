package onul.restapi.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;

@Entity
@Table(
        name = "analysis_exercise_volume_stats",
        indexes = {
                @Index(name = "idx_member_date_exercise", columnList = "member_id, recordDate, exercise_id"),
                @Index(name = "idx_detail_muscle_group", columnList = "detailMuscleGroup")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString
public class ExerciseVolumeStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보

    @Column(nullable = false)
    private LocalDate recordDate; // 날짜별 데이터 저장

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private String mainMuscleGroup;

    @Column(nullable = false)
    private String detailMuscleGroup;

    @Column(nullable = true)
    private double dailyVolume;


}
