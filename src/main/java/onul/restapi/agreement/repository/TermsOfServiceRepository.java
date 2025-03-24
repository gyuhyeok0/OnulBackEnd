package onul.restapi.agreement.repository;

import onul.restapi.agreement.entity.TermsOfService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermsOfServiceRepository extends JpaRepository<TermsOfService, Long> {
    Optional<TermsOfService> findByLanguage(String language);
}
