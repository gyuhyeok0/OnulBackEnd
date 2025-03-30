package onul.restapi.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;

@Entity
@Table(
        name = "member_last_login",
        indexes = {
                @Index(name = "idx_member_id", columnList = "member_id"),
                @Index(name = "idx_last_login_date", columnList = "last_login_date")
        }
)

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString
public class MemberLastLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Members member;

    @Column(name = "last_login_date", nullable = false)
    private LocalDate lastLoginDate;
}
