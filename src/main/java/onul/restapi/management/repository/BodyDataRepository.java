package onul.restapi.management.repository;

import onul.restapi.management.entity.BodyDataEntity;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BodyDataRepository extends JpaRepository<BodyDataEntity, Long> {

    Optional<BodyDataEntity> findByMemberAndDate(Members member, LocalDate date);

    boolean existsByMemberAndDate(Members member, LocalDate date);

    Optional<Object> findByMember_MemberIdAndDate(String memberId, LocalDate date);

    List<BodyDataEntity> findByMemberAndDateBetween(Members member, LocalDate monthStart, LocalDate monthEnd);

    void deleteByMember(Members member);

    @Query("SELECT b.date FROM BodyDataEntity b WHERE b.member = :member AND b.date BETWEEN :startDate AND :endDate")
    List<LocalDate> findBodyDates(@Param("member") Members member, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
