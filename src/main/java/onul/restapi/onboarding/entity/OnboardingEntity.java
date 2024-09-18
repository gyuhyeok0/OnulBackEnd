package onul.restapi.onboarding.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import onul.restapi.member.entity.Members;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "onboarding_info")
public class OnboardingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onboarding_id")
    private Long onboardingId;

    // Members 테이블과의 관계 설정 (외래키) - memberId를 String으로 변경
    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Members member;

    // 신체 정보
    @Column(name = "height")
    private double height;  // cm 또는 feet -> cm, 소수점 1자리까지 저장

    @Column(name = "height_unit")  // 키의 단위
    private String heightUnit;      // cm 또는 feet

    @Column(name = "weight")
    private double weight;  // kg 또는 lbs -> kg, 소수점 2자리까지 저장

    @Column(name = "weight_unit")   // 몸무게의 단위
    private String weightUnit;      // kg 또는 lbs

    // 운동 기록 (1rm)
    @Column(name = "bench_press_1rm")
    private int benchPress1rm;

    @Column(name = "squat_1rm")
    private int squat1rm;

    @Column(name = "deadlift_1rm")
    private int deadlift1rm;

    // 추가 필드: 성별
    @Column(name = "gender")
    private String gender;  // 남자, 여자, 기타

    // 추가 필드: 기본 단위 (몸무게 단위)
    @Column(name = "basic_unit")
    private String basicUnit;  // kg 또는 lbs
}
