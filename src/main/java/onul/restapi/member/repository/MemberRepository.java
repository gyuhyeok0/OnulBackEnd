package onul.restapi.member.repository;

import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Members, String> {
    Members findByMemberId(String memberId);
}
