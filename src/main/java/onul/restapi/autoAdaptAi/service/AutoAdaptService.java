package onul.restapi.autoAdaptAi.service;

import onul.restapi.autoAdaptAi.dto.AutoAdaptDTO;
import onul.restapi.autoAdaptAi.entity.AutoAdaptEntity;
import onul.restapi.autoAdaptAi.repository.AutoAdaptRepository;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.repository.ExerciseRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import onul.restapi.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AutoAdaptService {

    private final AutoAdaptRepository autoAdaptRepository;
    private final ExerciseRepository exerciseRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public AutoAdaptService(AutoAdaptRepository autoAdaptRepository, ExerciseRepository exerciseRepository, MemberRepository memberRepository, MemberService memberService) {
        this.autoAdaptRepository = autoAdaptRepository;
        this.exerciseRepository = exerciseRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @Transactional
    public AutoAdaptEntity saveOrUpdateAutoAdapt(AutoAdaptDTO autoAdaptDTO) {

        // ✅ 회원 조회
        String memberId = autoAdaptDTO.getMemberId();

        Members member = memberService.getMemberById(memberId);


        // ✅ `List<Integer>`가 들어올 가능성이 있으므로 `List<Long>`으로 변환
        List<Long> fixedExerciseList = autoAdaptDTO.getExercises().stream()
                .map(Long::valueOf)  // ✅ Integer → Long 변환
                .collect(Collectors.toList());

        // ✅ 운동 ID 리스트를 `Exercise` 엔티티 리스트로 변환
        List<Exercise> exerciseList = fixedExerciseList.stream()
                .map(exerciseId -> exerciseRepository.findById(exerciseId)
                        .orElseThrow(() -> new RuntimeException("Exercise ID " + exerciseId + " not found")))
                .collect(Collectors.toList());


        // ✅ 기존 데이터 조회 (memberId + date 기준으로 검색)
        Optional<AutoAdaptEntity> existingEntityOptional = autoAdaptRepository.findByMemberAndDate(member, autoAdaptDTO.getDate());

        if (existingEntityOptional.isPresent()) {
            // ✅ 기존 데이터가 존재하면 업데이트
            AutoAdaptEntity existingEntity = existingEntityOptional.get();
            existingEntity.setExercises(exerciseList);  // 운동 목록 업데이트
            return autoAdaptRepository.save(existingEntity);  // 업데이트된 데이터 저장
        } else {
            // ✅ 기존 데이터가 없으면 새로 생성하여 저장
            AutoAdaptEntity newEntity = AutoAdaptEntity.builder()
                    .exercises(exerciseList)
                    .date(autoAdaptDTO.getDate())
                    .member(member)
                    .build();

            return autoAdaptRepository.save(newEntity);
        }
    }


    public boolean existsAutoAdaptForToday(String memberId, LocalDate date) {
        return autoAdaptRepository.existsByMember_MemberIdAndDate(memberId, date);
    }

    @Transactional(readOnly = true)
    public List<Exercise> getExercises(AutoAdaptDTO request) {
        // ✅ 회원 조회
        String memberId = request.getMemberId();

        Members member = memberService.getMemberById(memberId);


        // ✅ AutoAdaptEntity 조회
        Optional<AutoAdaptEntity> autoAdaptEntityOptional = autoAdaptRepository.findByMemberAndDate(member, request.getDate());

        // ✅ 존재하면 exercises 리스트 반환, 없으면 빈 리스트 반환
        return autoAdaptEntityOptional.map(AutoAdaptEntity::getExercises)
                .orElseGet(Collections::emptyList);
    }





}
