package onul.restapi.notice.repository;

import onul.restapi.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findTopByOrderByIdDesc();
}
