package onul.restapi.exercise.repository;

import onul.restapi.exercise.entity.IntensityEntity;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface IntensityRepository extends JpaRepository<IntensityEntity, Long> {

    // Members 엔티티와 createdAt을 기준으로 IntensityEntity 조회
    IntensityEntity findByMemberAndCreatedAt(Members member, Date createdAt);

    void deleteByMember(Members member);
}
