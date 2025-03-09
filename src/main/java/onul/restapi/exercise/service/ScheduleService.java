package onul.restapi.exercise.service;

import onul.restapi.exercise.dto.ScheduleDTO;
import onul.restapi.exercise.entity.Schedule;
import onul.restapi.exercise.repository.ScheduleRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, MemberRepository memberRepository) {
        this.scheduleRepository = scheduleRepository;
        this.memberRepository = memberRepository;
    }


    @Transactional
    public void saveOrUpdateSchedule(ScheduleDTO scheduleDTO) {
        // Member 찾기 (예: 회원 ID로 조회)
        Members member = memberRepository.findById(scheduleDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 기존 Schedule 조회
        Schedule existingSchedule = scheduleRepository.findByMemberAndWeekTypeAndDayAndPart(
                member, scheduleDTO.getWeekType(), scheduleDTO.getDay(), scheduleDTO.getPart()
        ).orElse(null);

        if (existingSchedule != null) {
            // 업데이트: 기존 엔티티를 빌더로 수정
            Schedule updatedSchedule = Schedule.builder()
                    .id(existingSchedule.getId())
                    .weekType(scheduleDTO.getWeekType())
                    .day(scheduleDTO.getDay())
                    .part(scheduleDTO.getPart())
                    .member(member)
                    .build();
            scheduleRepository.save(updatedSchedule);
        } else {
            // 새로운 Schedule 생성 및 저장
            Schedule newSchedule = Schedule.builder()
                    .weekType(scheduleDTO.getWeekType())
                    .day(scheduleDTO.getDay())
                    .part(scheduleDTO.getPart())
                    .member(member)
                    .build();
            scheduleRepository.save(newSchedule);
        }
    }



    @Transactional
    public void deleteSchedule(ScheduleDTO scheduleDTO) {
        try {
            // 회원 정보 가져오기
            Members member = memberRepository.findById(scheduleDTO.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

            Optional<Schedule> scheduleOptional = scheduleRepository.findByMemberAndWeekTypeAndDayAndPart(
                    member, scheduleDTO.getWeekType(), scheduleDTO.getDay(), scheduleDTO.getPart());


            if (scheduleOptional.isPresent()) {
                Schedule schedule = scheduleOptional.get();
                scheduleRepository.delete(schedule);
            } else {
                throw new IllegalArgumentException("해당 스케줄이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 메시지 출력
            throw e; // 예외를 다시 던져서 클라이언트에게 알림
        }
    }



    // 특정 회원의 모든 스케줄을 조회하는 메서드
    public List<ScheduleDTO> getAllSchedulesByMemberId(String memberId) {
        // 회원 정보 가져오기
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 해당 회원의 모든 스케줄 조회
        List<Schedule> schedules = scheduleRepository.findAllByMember(member);

        // Schedule 엔티티를 ScheduleDTO로 변환하여 반환
        return schedules.stream()
                .map(schedule -> ScheduleDTO.builder()
                        .id(schedule.getId())
                        .weekType(schedule.getWeekType())
                        .day(schedule.getDay())
                        .part(schedule.getPart())
                        .memberId(memberId)  // ScheduleDTO에 memberId 설정
                        .build())
                .collect(Collectors.toList());
    }

}
