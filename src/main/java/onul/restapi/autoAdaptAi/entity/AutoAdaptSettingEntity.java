package onul.restapi.autoAdaptAi.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.member.entity.Members;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // 기존 객체를 빌더로 변경 가능하도록 추가
@Table(
        name = "auto_adapt_settings",
        uniqueConstraints = @UniqueConstraint(name = "unique_member", columnNames = {"member_id"})
)
public class AutoAdaptSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보

    @Column(nullable = false)
    private String exerciseGoal; // 운동 목표 (근비대 / 체지방 감소)

    @Column(nullable = false)
    private int exerciseSplit; // 분할 (1~6분할)

    @ElementCollection
    @CollectionTable(name = "auto_adapt_exercise_priority", joinColumns = @JoinColumn(name = "settings_id"))
    @Column(name = "priority")
    private List<String> priorityParts;


    @Column(nullable = false)
    private String difficulty; // 난이도 (초급 / 중급 / 고급)

    @Column(nullable = false)
    private String exerciseTime; // 운동 시간 (30분 이하 / 60분 이하 / 60분 이상)

    @ElementCollection
    @CollectionTable(name = "auto_adapt_exercise_styles", joinColumns = @JoinColumn(name = "settings_id"))
    @Column(name = "style")
    private List<String> exerciseStyle; // 운동 스타일 (머신, 프리웨이트, 맨몸)

    @ElementCollection
    @CollectionTable(name = "auto_adapt_excluded_parts", joinColumns = @JoinColumn(name = "settings_id"))
    @Column(name = "part")
    private List<String> excludedParts; // 특정 부위 제외 (nullable 허용)

    @Column(nullable = false)
    private boolean includeCardio; // 유산소 운동 포함 여부 (기본값 false)

}
