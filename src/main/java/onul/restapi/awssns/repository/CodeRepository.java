package onul.restapi.awssns.repository;

import onul.restapi.awssns.entity.CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<CodeEntity, Long> {
    CodeEntity findByPhoneNumber(String phoneNumber);

    void deleteByPhoneNumber(String phoneNumber);
}
