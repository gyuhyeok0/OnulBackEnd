package onul.restapi.analysis.repository;

import onul.restapi.analysis.entity.MemberLastLogin;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberLastLoginRepository extends JpaRepository<MemberLastLogin, Long> {

    Optional<MemberLastLogin> findByMember(Members member);

    List<MemberLastLogin> findByLastLoginDate(LocalDate today);

}
