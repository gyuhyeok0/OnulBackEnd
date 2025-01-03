package onul.restapi.management.repository;

import onul.restapi.management.entity.BodyDataEntity;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BodyDataRepository extends JpaRepository<BodyDataEntity, Long> {

    Optional<BodyDataEntity> findByMemberAndDate(Members member, LocalDate date);

}
