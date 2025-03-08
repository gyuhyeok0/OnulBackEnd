package onul.restapi.version.repository;

import onul.restapi.version.entity.AppVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersionEntity, Long> {
    // 가장 최신 버전 정보를 가져오는 메서드
    Optional<AppVersionEntity> findTopByOrderByIdDesc();
}
