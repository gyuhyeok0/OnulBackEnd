package onul.restapi.exercise.service;

import jakarta.transaction.Transactional;
import onul.restapi.exercise.dto.IntensityDTO;
import onul.restapi.exercise.entity.IntensityEntity;
import onul.restapi.exercise.repository.IntensityRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
public class IntensityService {

    private final IntensityRepository intensityRepository;
    private final MemberRepository memberRepository;

    public IntensityService(IntensityRepository intensityRepository, MemberRepository memberRepository) {
        this.intensityRepository = intensityRepository;
        this.memberRepository = memberRepository;
    }

    public IntensityDTO saveIntensity(String memberId, String intensity) {
        LocalDate today = LocalDate.now();
        Date todayStart = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        IntensityEntity existingIntensity = intensityRepository.findByMemberAndCreatedAt(member, todayStart);

        IntensityEntity result;
        if (existingIntensity != null) {
            result = intensityRepository.save(
                    IntensityEntity.builder()
                            .id(existingIntensity.getId())
                            .member(existingIntensity.getMember())
                            .intensity(intensity)
                            .createdAt(todayStart)
                            .build()
            );
        } else {
            result = intensityRepository.save(
                    IntensityEntity.builder()
                            .member(member)
                            .intensity(intensity)
                            .createdAt(todayStart)
                            .build()
            );
        }

        // DTO로 변환하여 반환
        return new IntensityDTO(
                result.getId(),
                result.getMember().getMemberId(), // memberId만 포함
                result.getIntensity(),
                result.getCreatedAt()
        );
    }


}
