package onul.restapi.onboarding.entity;

import jakarta.persistence.*;
import onul.restapi.member.entity.Members;

@Entity
@Table(name = "onboarding_info")
public class OnboardingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onboarding_id")
    private Long onboardingId;

    // Members 테이블과의 관계 설정 (외래키)
    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Members member;

    // 신체 정보
    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;


    // 운동 기록 (1RM)
    @Column(name = "bench_press_1rm")
    private Double benchPress1RM;

    @Column(name = "squat_1rm")
    private Double squat1RM;

    @Column(name = "deadlift_1rm")
    private Double deadlift1RM;


    public OnboardingEntity() {
    }

    public OnboardingEntity(Long onboardingId, Members member, Double height, Double weight, Double benchPress1RM, Double squat1RM, Double deadlift1RM) {
        this.onboardingId = onboardingId;
        this.member = member;
        this.height = height;
        this.weight = weight;
        this.benchPress1RM = benchPress1RM;
        this.squat1RM = squat1RM;
        this.deadlift1RM = deadlift1RM;
    }

    public Long getOnboardingId() {
        return onboardingId;
    }

    public void setOnboardingId(Long onboardingId) {
        this.onboardingId = onboardingId;
    }

    public Members getMember() {
        return member;
    }

    public void setMember(Members member) {
        this.member = member;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBenchPress1RM() {
        return benchPress1RM;
    }

    public void setBenchPress1RM(Double benchPress1RM) {
        this.benchPress1RM = benchPress1RM;
    }

    public Double getSquat1RM() {
        return squat1RM;
    }

    public void setSquat1RM(Double squat1RM) {
        this.squat1RM = squat1RM;
    }

    public Double getDeadlift1RM() {
        return deadlift1RM;
    }

    public void setDeadlift1RM(Double deadlift1RM) {
        this.deadlift1RM = deadlift1RM;
    }
}
