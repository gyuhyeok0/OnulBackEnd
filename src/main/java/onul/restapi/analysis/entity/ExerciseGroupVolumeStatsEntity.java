package onul.restapi.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;

@Entity
@Table(
        name = "exercise_group_volume_stats",
        indexes = {
                @Index(name = "idx_period_muscle_group", columnList = "startDate, periodType, mainMuscleGroup")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString
public class ExerciseGroupVolumeStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(nullable = false)
    private LocalDate startDate; // 주간 또는 월간 시작일

    @Column(nullable = false)
    private String periodType; // "WEEKLY" 또는 "MONTHLY"

    @Column(nullable = false)
    private String mainMuscleGroup; // 근육 그룹

    @Column(nullable = false)
    private double totalVolume; // 총 볼륨

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보
}
