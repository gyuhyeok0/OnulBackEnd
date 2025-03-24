package onul.restapi.agreement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "privacy_policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivacyPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예: "ko", "en", "ja"
    @Column(length = 10, nullable = false)
    private String language;

    // HTML 형식의 본문 내용
    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;
}
