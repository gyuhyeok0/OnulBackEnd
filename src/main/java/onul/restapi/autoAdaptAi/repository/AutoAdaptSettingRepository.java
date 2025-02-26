package onul.restapi.autoAdaptAi.repository;

import onul.restapi.autoAdaptAi.entity.AutoAdaptSettingEntity;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutoAdaptSettingRepository extends JpaRepository<AutoAdaptSettingEntity, Long> {


    Optional<Object> findByMember(Members member);


    void deleteByMember(Members member);
}
