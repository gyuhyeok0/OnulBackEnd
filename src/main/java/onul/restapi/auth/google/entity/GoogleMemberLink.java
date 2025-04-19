package onul.restapi.auth.google.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_google_member_link", uniqueConstraints = {
        @UniqueConstraint(name = "uk_google_id", columnNames = "google_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleMemberLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_id", nullable = false, unique = true)
    private String googleId;

    @Column(name = "member_id", nullable = false)
    private String memberId;
}
