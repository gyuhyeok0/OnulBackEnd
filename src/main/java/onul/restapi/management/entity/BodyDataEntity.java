package onul.restapi.management.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;

@Entity
@Table(
        name = "body_data",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_member_date", columnNames = {"member_id", "date"})
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BodyDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보

    @Column(nullable = true)
    private Double weight; // 체중 (kg)
    @Column(nullable = true)
    private Double weightInLbs; // 체중 (lbs)
    @Column(nullable = true)
    private Double skeletalMuscleMass; // 골격근량 (kg)

    @Column(nullable = true)
    private Double skeletalMuscleMassInLbs; // 골격근량 (lbs)

    @Column(nullable = true)
    private Double bodyFatMass; // 체지방량 (kg)
    @Column(nullable = true)
    private Double bodyFatMassInLbs; // 체지방량 (lbs)
    @Column(nullable = true)
    private Double bodyFatPercentage; // 체지방률 (%)
    @Column(nullable = false)
    private LocalDate date; // 데이터 생성 날짜
}
