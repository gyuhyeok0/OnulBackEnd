package onul.restapi.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;

@Entity
@Table(name = "weight_and_diet_statistics", indexes = {
        @Index(name = "idx_member_date", columnList = "member_id, date")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString
public class WeightAndDietStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보

    @Column(nullable = false)
    private LocalDate date; // 통계 기준 날짜 (월의 첫 번째 날)

    @Column(nullable = true)
    private Double averageWeight; // 평균 몸무게

    @Column(nullable = true)
    private Double averageBodyFatMass; // 평균 체지방량

    @Column(nullable = true)
    private Double averageBodyFatPercentage; // 평균 체지방률

    @Column(nullable = true)
    private Double averageSkeletalMuscleMass; // 평균 골격근량

    @Column(nullable = true)
    private Double averageCalories; // 평균 칼로리 섭취량

    @Column(nullable = true)
    private Double averageProtein; // 평균 단백질 섭취량

    @Column(nullable = true)
    private Double averageCarbohydrates; // 평균 탄수화물 섭취량

}
