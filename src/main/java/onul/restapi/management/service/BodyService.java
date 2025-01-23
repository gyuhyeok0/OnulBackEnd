package onul.restapi.management.service;

import jakarta.transaction.Transactional;
import onul.restapi.management.dto.BodyDataDto;
import onul.restapi.management.entity.BodyDataEntity;
import onul.restapi.management.repository.BodyDataRepository;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class BodyService {

    private final BodyDataRepository bodyDataRepository;
    private final MemberRepository memberRepository;


    public BodyService(BodyDataRepository bodyDataRepository, MemberRepository memberRepository) {
        this.bodyDataRepository = bodyDataRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public BodyDataDto saveBodyData(String memberId, BodyDataDto bodyDataDto) {
        // Member 엔티티를 먼저 조회
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));

        // 오늘 날짜의 데이터가 이미 존재하는지 확인
        Optional<BodyDataEntity> existingEntity = bodyDataRepository.findByMemberAndDate(member, bodyDataDto.getDate());

        BodyDataEntity bodyDataEntity;

        if (existingEntity.isPresent()) {
            // 데이터가 존재하면 업데이트
            BodyDataEntity existingData = existingEntity.get();

            bodyDataEntity = BodyDataEntity.builder()
                    .id(existingData.getId()) // 기존 ID 유지
                    .member(existingData.getMember()) // 기존 Member 유지
                    .weight(bodyDataDto.getWeight() != null ? bodyDataDto.getWeight() : existingData.getWeight())
                    .weightInLbs(bodyDataDto.getWeightInLbs() != null ? bodyDataDto.getWeightInLbs() : existingData.getWeightInLbs())
                    .skeletalMuscleMass(bodyDataDto.getSkeletalMuscleMass() != null ? bodyDataDto.getSkeletalMuscleMass() : existingData.getSkeletalMuscleMass())
                    .skeletalMuscleMassInLbs(bodyDataDto.getSkeletalMuscleMassInLbs() != null ? bodyDataDto.getSkeletalMuscleMassInLbs() : existingData.getSkeletalMuscleMassInLbs())
                    .bodyFatMass(bodyDataDto.getBodyFatMass() != null ? bodyDataDto.getBodyFatMass() : existingData.getBodyFatMass())
                    .bodyFatMassInLbs(bodyDataDto.getBodyFatMassInLbs() != null ? bodyDataDto.getBodyFatMassInLbs() : existingData.getBodyFatMassInLbs())
                    .bodyFatPercentage(bodyDataDto.getBodyFatPercentage() != null ? bodyDataDto.getBodyFatPercentage() : existingData.getBodyFatPercentage())
                    .date(existingData.getDate()) // 기존 날짜 유지
                    .build();
        } else {
            // 데이터가 없으면 새로 생성
            bodyDataEntity = BodyDataEntity.builder()
                    .member(member)
                    .weight(bodyDataDto.getWeight())
                    .weightInLbs(bodyDataDto.getWeightInLbs())
                    .skeletalMuscleMass(bodyDataDto.getSkeletalMuscleMass())
                    .skeletalMuscleMassInLbs(bodyDataDto.getSkeletalMuscleMassInLbs())
                    .bodyFatMass(bodyDataDto.getBodyFatMass())
                    .bodyFatMassInLbs(bodyDataDto.getBodyFatMassInLbs())
                    .bodyFatPercentage(bodyDataDto.getBodyFatPercentage())
                    .date(bodyDataDto.getDate())
                    .build();
        }


        // 데이터 저장
        BodyDataEntity savedEntity = bodyDataRepository.save(bodyDataEntity);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return new BodyDataDto(
                savedEntity.getMember().getMemberId(),
                savedEntity.getWeight(),
                savedEntity.getWeightInLbs(),
                savedEntity.getSkeletalMuscleMass(),
                savedEntity.getSkeletalMuscleMassInLbs(),
                savedEntity.getBodyFatMass(),
                savedEntity.getBodyFatMassInLbs(),
                savedEntity.getBodyFatPercentage(),
                savedEntity.getDate()
        );
    }

    @Transactional
    public BodyDataDto getBodyRecordsForDate(String memberId, LocalDate date) {

        // memberId로 회원 조회
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));

        // memberId와 date를 기준으로 BodyData 조회
        BodyDataEntity bodyData = (BodyDataEntity) bodyDataRepository.findByMember_MemberIdAndDate(memberId, date)
                .orElseThrow(() -> new IllegalArgumentException("No body record found for the given member and date."));

        // BodyData를 BodyDataDto로 변환 후 반환
        return new BodyDataDto(
                bodyData.getMember().getMemberId(),            // 회원 ID
                bodyData.getWeight(),                          // 체중
                bodyData.getWeightInLbs(),                     // 체중 (lbs)
                bodyData.getSkeletalMuscleMass(),              // 골격근량
                bodyData.getSkeletalMuscleMassInLbs(),         // 골격근량 (lbs)
                bodyData.getBodyFatMass(),                     // 체지방량
                bodyData.getBodyFatMassInLbs(),                // 체지방량 (lbs)
                bodyData.getBodyFatPercentage(),               // 체지방률
                bodyData.getDate()                             // 날짜
        );
    }


}
