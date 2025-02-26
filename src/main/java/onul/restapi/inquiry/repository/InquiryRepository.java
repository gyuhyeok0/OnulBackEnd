package onul.restapi.inquiry.repository;

import onul.restapi.inquiry.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {


    void deleteByMemberId(String memberId);

}
