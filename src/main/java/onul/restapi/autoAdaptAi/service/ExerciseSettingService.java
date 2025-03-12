package onul.restapi.autoAdaptAi.service;

import onul.restapi.autoAdaptAi.dto.AutoAdaptSettingDTO;
import onul.restapi.autoAdaptAi.dto.AutoAdaptSettingRequstDTO;
import onul.restapi.autoAdaptAi.dto.PriorityPartsRequestDTO;
import onul.restapi.autoAdaptAi.entity.AutoAdaptSettingEntity;
import onul.restapi.autoAdaptAi.repository.AutoAdaptSettingRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExerciseSettingService {



    private final AutoAdaptSettingRepository autoAdaptSettingRepository;
    private final MemberRepository memberRepository;

    public ExerciseSettingService(AutoAdaptSettingRepository autoAdaptSettingRepository, MemberRepository memberRepository) {
        this.autoAdaptSettingRepository = autoAdaptSettingRepository;
        this.memberRepository = memberRepository;
    }

    public void autoAdaptDefaultSetting(String memberId) {

        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));

        // 이미 존재하는 AutoAdaptSettingEntity가 있는지 확인
        boolean exists = autoAdaptSettingRepository.existsByMember(member);

        if (exists) {
            return; // 이미 존재하면 아무 작업도 하지 않음
        }

        AutoAdaptSettingEntity defaultSetting = AutoAdaptSettingEntity.builder()
                .member(member)
                .exerciseGoal("근비대")  // 기본값: 근비대
                .exerciseSplit(3)  // 기본값: 4분할
                .priorityParts(List.of("자동"))
                .difficulty("중급")  // 기본값: 중급
                .exerciseTime("60분 이하")  // 기본값: 60분 이하
                .exerciseStyle(List.of("머신", "프리웨이트"))  // 기본값: 머신 + 프리웨이트
                .excludedParts(null)  // 기본값: 없음 (nullable 허용)
                .includeCardio(false)  // 기본값: 유산소 운동 제외
                .build();

        autoAdaptSettingRepository.save(defaultSetting);
    }


    public AutoAdaptSettingDTO selectAutoAdaptSetting(String memberId) {

        // 1. 회원 조회 (예외 처리)
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));

        // 2. 설정 조회
        AutoAdaptSettingEntity entity = (AutoAdaptSettingEntity) autoAdaptSettingRepository.findByMember(member)
                .orElse(null);

        // 3. 설정이 없으면 빈 DTO 반환
        if (entity == null) {
            return new AutoAdaptSettingDTO(); // ✅ 빈 DTO 반환하여 JSON 파싱 오류 방지
        }

        // 4. 설정이 있으면 DTO 변환 후 반환
        return new AutoAdaptSettingDTO(
                entity.getExerciseGoal(),
                entity.getExerciseSplit(),
                entity.getPriorityParts(),
                entity.getDifficulty(),
                entity.getExerciseTime(),
                entity.getExerciseStyle(),
                entity.getExcludedParts(),
                entity.isIncludeCardio()
        );
    }


    @Transactional
    public void updateAutoAdaptSetting(AutoAdaptSettingRequstDTO request) {

        // 1. 회원 조회 (예외 처리)
        Members member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + request.getMemberId()));

        // 2. 기존 자동적응 설정 조회 (Optional 처리)
        AutoAdaptSettingEntity existingSetting = (AutoAdaptSettingEntity) autoAdaptSettingRepository.findByMember(member).orElse(null);

        // 3. 빌더 패턴을 활용한 업데이트 (Optional 제거 후 `toBuilder()` 사용)
        AutoAdaptSettingEntity updatedSetting = (existingSetting != null)
                ? existingSetting.toBuilder()
                .exerciseGoal(request.getExerciseGoal())
                .exerciseSplit(request.getExerciseSplit())
                .priorityParts(request.getPriorityParts())
                .difficulty(request.getDifficulty())
                .exerciseTime(request.getExerciseTime())
                .exerciseStyle(request.getExerciseStyle())
                .excludedParts(request.getExcludedParts())
                .includeCardio(request.isIncludeCardio())
                .build()
                : AutoAdaptSettingEntity.builder()
                .member(member)
                .exerciseGoal(request.getExerciseGoal())
                .exerciseSplit(request.getExerciseSplit())
                .priorityParts(request.getPriorityParts())
                .difficulty(request.getDifficulty())
                .exerciseTime(request.getExerciseTime())
                .exerciseStyle(request.getExerciseStyle())
                .excludedParts(request.getExcludedParts())
                .includeCardio(request.isIncludeCardio())
                .build();

        // 4. 저장
        autoAdaptSettingRepository.save(updatedSetting);
    }



    @Transactional
    public void changePriority(PriorityPartsRequestDTO request) {


        // 1. 회원 조회 (예외 처리)
        Members member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + request.getMemberId()));

        // 2. 기존 자동적응 설정 조회 (없으면 예외 처리)
        AutoAdaptSettingEntity existingSetting = (AutoAdaptSettingEntity) autoAdaptSettingRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("AutoAdaptSetting not found for member ID: " + request.getMemberId()));

        // 3. String → List<String> 변환
        List<String> priorityPartsList = (request.getPriorityParts() == null || request.getPriorityParts().trim().isEmpty())
                ? Collections.emptyList() // 빈 문자열 또는 null이면 빈 리스트로 처리
                : Arrays.asList(request.getPriorityParts().split("\\s*,\\s*")); // 쉼표 기준으로 분할 (공백 제거)


        // 4. 기존 설정에서 priorityParts만 변경
        AutoAdaptSettingEntity updatedSetting = existingSetting.toBuilder()
                .priorityParts(priorityPartsList) // ✅ 여기서 priorityParts만 업데이트
                .build();

        // 5. 변경된 엔티티 저장
        autoAdaptSettingRepository.save(updatedSetting);
    }


    @Transactional
    public void changeDifficultySetting(String memberId, String difficulty) {
        // 1. 회원 조회 (예외 처리)
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));

        // 2. 기존 자동적응 설정 조회 (Optional 처리)
        AutoAdaptSettingEntity existingSetting = (AutoAdaptSettingEntity) autoAdaptSettingRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("AutoAdapt setting not found for member: " + memberId));

        // 3. `difficulty`만 업데이트
        AutoAdaptSettingEntity updatedSetting = existingSetting.toBuilder()
                .difficulty(difficulty)
                .build();

        // 4. 저장
        autoAdaptSettingRepository.save(updatedSetting);
    }


    @Transactional
    public void updatePriorityDefaltSetting(PriorityPartsRequestDTO request) {


        // 1. 회원 조회 (예외 처리)
        Members member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + request.getMemberId()));

        // 2. 기존 자동적응 설정 조회 (없으면 예외 처리)
        AutoAdaptSettingEntity existingSetting = (AutoAdaptSettingEntity) autoAdaptSettingRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("AutoAdaptSetting not found for member ID: " + request.getMemberId()));

        // 3. String → List<String> 변환
        List<String> priorityPartsList = (request.getPriorityParts() == null || request.getPriorityParts().trim().isEmpty())
                ? Collections.emptyList() // 빈 문자열 또는 null이면 빈 리스트로 처리
                : Arrays.asList(request.getPriorityParts().split("\\s*,\\s*")); // 쉼표 기준으로 분할 (공백 제거)


        // 4. 기존 설정에서 priorityParts만 변경
        AutoAdaptSettingEntity updatedSetting = existingSetting.toBuilder()
                .priorityParts(priorityPartsList) // ✅ 여기서 priorityParts만 업데이트
                .build();

        // 5. 변경된 엔티티 저장
        autoAdaptSettingRepository.save(updatedSetting);

    }



}
