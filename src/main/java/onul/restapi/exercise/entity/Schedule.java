package onul.restapi.exercise.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.member.entity.Members;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_schedules", indexes = {
        @Index(name = "idx_member_weekType_day_part", columnList = "member_id, weekType, day, part")
})
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String weekType;

    @Column(nullable = false)
    private String day;

    @Column(name = "part")
    private String part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member;

    // 빌더에서 사용할 생성자
    @Builder
    public Schedule(String weekType, String day, String part, Members member) {
        this.weekType = weekType;
        this.day = day;
        this.part = part;
        this.member = member;
    }
}
