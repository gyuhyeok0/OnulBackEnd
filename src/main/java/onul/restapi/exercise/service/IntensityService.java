package onul.restapi.exercise.service;

import onul.restapi.exercise.dto.IntensityDTO;
import onul.restapi.exercise.entity.IntensityEntity;
import onul.restapi.exercise.repository.IntensityRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import onul.restapi.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
public class IntensityService {

    private final IntensityRepository intensityRepository;
    private final MemberService memberService;

    public IntensityService(IntensityRepository intensityRepository, MemberService memberService) {
        this.intensityRepository = intensityRepository;
        this.memberService = memberService;
    }

    @Transactional
    public IntensityDTO saveIntensity(String memberId, String intensity) {
        LocalDate today = LocalDate.now();
        Date todayStart = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Members member = memberService.getMemberById(memberId);

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
