package onul.restapi.member.repository;

import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Members, String> {
    Members findByMemberId(String memberId);

    // sms 인종코드 발송시 전화번호 확인
    boolean existsByMemberPhoneNumber(String phoneNumber);
}
