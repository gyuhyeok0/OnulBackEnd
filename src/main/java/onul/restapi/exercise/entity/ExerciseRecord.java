package onul.restapi.exercise.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.exercise.dto.ExerciseDto;
import onul.restapi.exercise.dto.SetDTO;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;

@Entity
@Table(
        name = "exercise_record",
        indexes = {
                @Index(name = "idx_record_date", columnList = "record_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_record_constraint",
                        columnNames = {"record_date", "exercise_service_id", "exercise_record_id", "set_number"}
                )
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ExerciseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_record_id")
    private Long exerciseRecordId; // 이름 변경

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보

    @Column(nullable = false)
    private int setNumber; // 세트 번호

    @Embedded
    private SetDTO set; // 세트 정보

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "exercise_type_id", nullable = false)
    private ExerciseType exerciseType; // 연관된 ExerciseType 엔티티

    @ManyToOne
    @JoinColumn(name = "exercise_service_id", nullable = false)
    private ExerciseServiceNumber exerciseServiceNumber; // 연관된 ExerciseService 엔티티

    @Column(nullable = true)
    private String volume; // 원본 volume 값

    @Column(nullable = true)
    private String weightUnit; // 무게 단위 (kg, lbs)

    @Column(nullable = true)
    private String kmUnit; // 거리 단위 (km, mi)

    @Column(nullable = true)
    private Double kmVolume; // km 단위 거리

    @Column(nullable = true)
    private Double miVolume; // 마일 단위 거리

    @Column(nullable = true)
    private Double kgVolume; // kg 단위 무게

    @Column(nullable = true)
    private Double lbsVolume; // lbs 단위 무게

    @Column(nullable = true)
    private String timeVolume; // TIME 운동 시간

    @Column(nullable = true)
    private int repsVolume; // REPETITION 반복 횟수

    @Column(nullable = true)
    private LocalDate recordDate; // 기록 날짜
}
