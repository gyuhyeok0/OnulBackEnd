package onul.restapi.auth.apple.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_apple_member_link", uniqueConstraints = {
        @UniqueConstraint(name = "uk_apple_id", columnNames = "apple_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppleMemberLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apple_id", nullable = false, unique = true)
    private String appleId;

    @Column(name = "member_id", nullable = false)
    private String memberId;
}
