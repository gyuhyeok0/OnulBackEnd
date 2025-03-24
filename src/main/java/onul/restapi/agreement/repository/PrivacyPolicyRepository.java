package onul.restapi.agreement.repository;

import onul.restapi.agreement.entity.PrivacyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivacyPolicyRepository extends JpaRepository<PrivacyPolicy, Long> {
    Optional<PrivacyPolicy> findByLanguage(String language);
}
