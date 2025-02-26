package onul.restapi.awssns.repository;

import onul.restapi.awssns.entity.CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<CodeEntity, Long> {
    CodeEntity findByPhoneNumber(String phoneNumber);

    void deleteByPhoneNumber(String phoneNumber);

    List<CodeEntity> findByExpiryTimeBefore(long expiryTime);

}
