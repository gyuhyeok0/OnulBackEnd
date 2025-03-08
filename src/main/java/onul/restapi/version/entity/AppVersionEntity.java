package onul.restapi.version.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "app_version")
public class AppVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String version; // 최신 앱 버전

    public AppVersionEntity(String version) {
        this.version = version;
    }

    public void updateVersion(String newVersion) {
        this.version = newVersion;
    }
}
