package onul.restapi.notice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active;

    @Column(name = "title_ko")
    private String titleKo;

    @Column(name = "content_ko", columnDefinition = "TEXT")
    private String contentKo;

    @Column(name = "title_en")
    private String titleEn;

    @Column(name = "content_en", columnDefinition = "TEXT")
    private String contentEn;

    @Column(name = "title_ja")
    private String titleJa;

    @Column(name = "content_ja", columnDefinition = "TEXT")
    private String contentJa;

    @Column(name = "title_es")
    private String titleEs;

    @Column(name = "content_es", columnDefinition = "TEXT")
    private String contentEs;
}
