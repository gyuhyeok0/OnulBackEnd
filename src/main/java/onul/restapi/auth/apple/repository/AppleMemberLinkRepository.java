package onul.restapi.auth.apple.repository;

import onul.restapi.auth.apple.entity.AppleMemberLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppleMemberLinkRepository extends JpaRepository<AppleMemberLink, Long> {
    Optional<AppleMemberLink> findByAppleId(String appleId);
}
