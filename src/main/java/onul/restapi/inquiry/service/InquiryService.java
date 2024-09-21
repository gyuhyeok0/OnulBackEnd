package onul.restapi.inquiry.service;

import jakarta.transaction.Transactional;
import onul.restapi.inquiry.dto.InquiryDTO;
import onul.restapi.inquiry.entity.InquiryEntity;
import onul.restapi.inquiry.repository.InquiryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class InquiryService {


    private final InquiryRepository inquiryRepository;

    public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    // 문의 데이터 저장
    @Transactional
    public void submitInquiry(InquiryDTO inquiryDTO) {
        // 1. InquiryEntity로 변환
        InquiryEntity inquiryEntity = new InquiryEntity();
        inquiryEntity.setMemberId(inquiryDTO.getMemberId());
        inquiryEntity.setEmail(inquiryDTO.getEmail());
        inquiryEntity.setTitle(inquiryDTO.getTitle());
        inquiryEntity.setContent(inquiryDTO.getContent());
        inquiryEntity.setCreatedAt(LocalDateTime.now());  // 여기에서 createdAt을 설정

        // 2. 데이터베이스에 저장 (Prepared Statement는 JpaRepository에서 내부적으로 처리됨)
        inquiryRepository.save(inquiryEntity);
    }

}
