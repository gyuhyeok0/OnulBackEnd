package onul.restapi.exercise.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onul.restapi.member.entity.Members;

import java.util.Date;

@Entity
@Table(name = "tbl_intensity", indexes = {
        @Index(name = "idx_created_at", columnList = "created_at") // created_at에 인덱스 추가
})@Getter
@NoArgsConstructor
public class IntensityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne 관계 설정
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member;

    private String intensity;

    @Temporal(TemporalType.DATE) // 날짜만 저장하도록 설정
    @Column(name = "created_at") // 컬럼 이름 설정
    private Date createdAt;

    @Builder
    public IntensityEntity(Long id, Members member, String intensity, Date createdAt) {
        this.id = id;
        this.member = member;
        this.intensity = intensity;
        this.createdAt = createdAt;
    }
}
