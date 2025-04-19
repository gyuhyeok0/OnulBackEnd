package onul.restapi.auth.google.repository;

import onul.restapi.auth.google.entity.GoogleMemberLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleMemberLinkRepository extends JpaRepository<GoogleMemberLink, Long> {

    Optional<GoogleMemberLink> findByGoogleId(String googleId);
}
