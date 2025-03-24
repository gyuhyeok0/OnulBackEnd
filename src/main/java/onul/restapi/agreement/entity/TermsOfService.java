package onul.restapi.agreement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "terms_of_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermsOfService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 언어 코드 (예: "ko", "en", "ja")
    @Column(length = 10, nullable = false)
    private String language;

    // HTML 내용
    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;
}
