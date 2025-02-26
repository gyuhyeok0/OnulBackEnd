package onul.restapi.autoAdaptAi.repository;

import onul.restapi.autoAdaptAi.entity.AutoAdaptEntity;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AutoAdaptRepository extends JpaRepository<AutoAdaptEntity, Long> {


    Optional<AutoAdaptEntity> findByMemberAndDate(Members member, LocalDate date);

    boolean existsByMember_MemberIdAndDate(String memberId, LocalDate date);


    void deleteByMember(Members member);
}
