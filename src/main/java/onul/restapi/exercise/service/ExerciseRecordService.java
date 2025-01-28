package onul.restapi.exercise.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import onul.restapi.exercise.dto.ExerciseDto;
import onul.restapi.exercise.dto.ExerciseRecordDTO;
import onul.restapi.exercise.dto.ExerciseVolumeRequest;
import onul.restapi.exercise.dto.SetDTO;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.entity.ExerciseRecord;
import onul.restapi.exercise.entity.ExerciseServiceNumber;
import onul.restapi.exercise.entity.ExerciseType;
import onul.restapi.exercise.repository.ExerciseRecordRepository;
import onul.restapi.exercise.repository.ExerciseRepository;
import onul.restapi.exercise.repository.ExerciseServiceRepository;
import onul.restapi.exercise.repository.ExerciseTypeRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseRecordService {

    private final ExerciseRecordRepository exerciseRecordRepository;
    private final MemberRepository memberRepository;
    private final ExerciseTypeRepository exerciseTypeRepository;
    private final ExerciseServiceRepository exerciseServiceRepository;
    private final ExerciseRepository exerciseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ExerciseRecordService(ExerciseRecordRepository exerciseRecordRepository, MemberRepository memberRepository, ExerciseTypeRepository exerciseTypeRepository, ExerciseServiceRepository exerciseServiceRepository, ExerciseRepository exerciseRepository) {
        this.exerciseRecordRepository = exerciseRecordRepository;
        this.memberRepository = memberRepository;
        this.exerciseTypeRepository = exerciseTypeRepository;
        this.exerciseServiceRepository = exerciseServiceRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public void saveExerciseRecord(ExerciseRecordDTO exerciseRecord) {
        // 원본 volume 값을 String 타입으로 가져오기
        String volumeString = exerciseRecord.getVolume();
        Object parsedVolume;

        // DISTANCE와 WEIGHT를 위한 계산된 volume 변수 초기화
        double kmVolume = 0.0;
        double miVolume = 0.0;
        double kgVolume = 0.0;
        double lbsVolume = 0.0;

        // TIME과 REPETITION을 위한 추가 변수
        String timeVolume = null;
        int repsVolume = 0;

        // exerciseType에 따라 volume 타입 변환
        switch (exerciseRecord.getExerciseType()) {
            case 1: // REPETITION
                break;
            case 2: // DISTANCE
                break;
            case 3: // WEIGHT
                try {
                    parsedVolume = Double.parseDouble(volumeString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("WEIGHT에 유효하지 않은 volume 값: " + volumeString);
                }
                if ("kg".equals(exerciseRecord.getWeightUnit())) {
                    kgVolume = roundToTwoDecimalPlaces((Double) parsedVolume);
                    lbsVolume = roundToTwoDecimalPlaces(kgVolume * 2.20462);
                } else if ("lbs".equals(exerciseRecord.getWeightUnit())) {
                    lbsVolume = roundToTwoDecimalPlaces((Double) parsedVolume);
                    kgVolume = roundToTwoDecimalPlaces(lbsVolume * 0.453592);
                }
                break;
            case 4: // TIME
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 exerciseType: " + exerciseRecord.getExerciseType());
        }

        // 오늘 날짜 설정
        LocalDate today = LocalDate.now();
        exerciseRecord.setRecordDate(today);

        // 엔티티 조회
        Members member = memberRepository.findById(exerciseRecord.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        ExerciseServiceNumber exerciseServiceNumberEntity = exerciseServiceRepository.findById((long) exerciseRecord.getExerciseService())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ExerciseService ID"));
        ExerciseType exerciseTypeEntity = exerciseTypeRepository.findById((long) exerciseRecord.getExerciseType())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ExerciseType ID"));
        Exercise exerciseEntity = exerciseRepository.findById(exerciseRecord.getExercise().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Exercise ID"));

        // 중복 확인
        ExerciseRecord existingRecord = exerciseRecordRepository.findByRecordDateAndExerciseServiceNumberAndSetNumberAndExercise(
                today,
                exerciseServiceNumberEntity,
                exerciseRecord.getSetNumber(),
                exerciseEntity // 운동 ID가 포함된 Exercise 엔티티
        ).orElse(null);

        if (existingRecord != null) {
            // 기존 데이터 업데이트 (빌더 사용)
            exerciseRecordRepository.delete(existingRecord);

        }
            // 새로운 데이터 생성 (빌더 사용)
            ExerciseRecord newRecord = ExerciseRecord.builder()
                    .member(member)
                    .exerciseServiceNumber(exerciseServiceNumberEntity)
                    .setNumber(exerciseRecord.getSetNumber())
                    .set(exerciseRecord.getSet())
                    .exercise(exerciseEntity)
                    .exerciseType(exerciseTypeEntity)
                    .volume(volumeString)
//                    .repsVolume(repsVolume)
//                    .kmVolume(kmVolume)
//                    .miVolume(miVolume)
                    .kgVolume(kgVolume)
//                    .lbsVolume(lbsVolume)
//                    .timeVolume(timeVolume)
                    .recordDate(today)
                    .weightUnit(exerciseRecord.getWeightUnit())
//                    .kmUnit(exerciseRecord.getKmUnit())
                    .build();

            exerciseRecordRepository.save(newRecord);
            System.out.println("새로운 운동 기록이 저장되었습니다: " + newRecord.getExerciseRecordId());

    }


    // 소수점 두 자리로 반올림하는 메서드
    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }



    public void deleteExerciseRecord(ExerciseRecordDTO exerciseRecord) {

        System.out.println("서비스 숫자"+exerciseRecord.getExerciseService());

        LocalDate today = LocalDate.now();

        Members member = memberRepository.findById(exerciseRecord.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        Exercise exercise = exerciseRepository.findById(exerciseRecord.getExercise().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Exercise ID가 존재하지 않습니다."));

        // ExerciseServiceNumber 엔티티를 exerciseService ID로 조회
        ExerciseServiceNumber exerciseServiceNumber = exerciseServiceRepository.findById((long) exerciseRecord.getExerciseService())
                .orElseThrow(() -> new IllegalArgumentException("해당 ExerciseService ID가 존재하지 않습니다."));

        // 조건에 맞는 기록 조회
        ExerciseRecord record = exerciseRecordRepository.findByMemberAndExerciseAndExerciseServiceNumberAndSetNumberAndRecordDate(
                member,
                exercise,
                exerciseServiceNumber,
                exerciseRecord.getSetNumber(),
                today
        ).orElseThrow(() -> new IllegalArgumentException("해당 조건에 맞는 운동 기록이 존재하지 않습니다."));

        // 기록 삭제
        exerciseRecordRepository.delete(record);

        System.out.println("삭제된 운동 기록 ID: " + record.getExerciseRecordId());
    }



    // 운동 기록 검색
    public List<ExerciseRecordDTO> searchExerciseRecords(
            Long exerciseId, String memberId, int exerciseService, LocalDate recordDate) {

        // ExerciseRecord 엔티티를 검색
        List<ExerciseRecord> exerciseRecords = exerciseRepository.findRecordsByConditions(
                exerciseId, memberId, exerciseService, recordDate
        );

        // ExerciseRecord 엔티티를 ExerciseRecordDTO로 변환 후 반환
        return exerciseRecords.stream()
                .map(record -> new ExerciseRecordDTO(
                        record.getExerciseRecordId(), // ID 필드 매핑 (getExerciseRecordId 사용)
                        record.getMember().getMemberId(), // Member ID 매핑
                        record.getSetNumber(), // 세트 번호 매핑
                        new SetDTO( // SetDTO 생성
                                record.getSet().getCompleted(), // 완료 여부
                                record.getSet().getKg(),        // 킬로그램
                                record.getSet().getKm(),        // 킬로미터
                                record.getSet().getLbs(),       // 파운드
                                record.getSet().getMi(),        // 마일
                                record.getSet().getReps(),      // 반복 횟수
                                record.getSet().getTime()       // 시간
                        ),
                        new ExerciseDto( // ExerciseDto 생성
                                record.getExercise().getId(), // 운동 ID
                                record.getExercise().getExerciseName(), // 운동 이름
                                record.getExercise().getMainMuscleGroup(), // 주요 근육 그룹
                                record.getExercise().getDetailMuscleGroup(), // 세부 근육 그룹
                                record.getExercise().getPopularityGroup(), // 인기 그룹 여부
                                record.getExercise().getIsLiked() // 좋아요 상태
                        ),
                        record.getExerciseServiceNumber().getId(), // 운동 서비스 ID 매핑
                        record.getExerciseType().getId(), // 운동 타입 ID 매핑
                        record.getVolume(), // 볼륨 매핑
                        record.getWeightUnit(), // 무게 단위 매핑
//                        record.getKmUnit(), // 거리 단위 매핑
                        record.getRecordDate(), // 기록 날짜 매핑
//                        record.getKmVolume(), // km 볼륨 매핑
//                        record.getMiVolume(), // 마일 볼륨 매핑
                        record.getKgVolume() // kg 볼륨 매핑
//                        record.getLbsVolume(), // lbs 볼륨 매핑
//                        record.getTimeVolume(), // 시간 매핑
//                        record.getRepsVolume() // 반복 횟수 매핑
                ))
                .toList();
    }

    public List<LocalDate> getPreviousRecordDates(String memberId, Long exerciseId, Integer exerciseService) {

        LocalDate currentDate = LocalDate.now(); // 현재 날짜
        LocalDate threeMonthsAgo = currentDate.minusMonths(3); // 3달 전 날짜

        // 최근 3달 데이터 조회
        List<LocalDate> recentDates = exerciseRecordRepository.findRecentRecordDates(
                memberId, exerciseId, exerciseService, threeMonthsAgo);

        // 최근 3달 데이터가 없는 경우 전체 데이터 반환
        if (recentDates.isEmpty()) {
            return exerciseRecordRepository.findDistinctRecordDatesByMemberIdAndExerciseIdAndServiceNumber(
                    memberId, exerciseId, exerciseService);
        }

        return recentDates;
    }


//    public LocalDate getMostPreviousRecordDates(String memberId, Long exerciseId, Integer exerciseService) {
//        return exerciseRecordRepository.findMostRecentRecordDateByMemberIdAndExerciseIdAndServiceNumber(
//                memberId, exerciseId, exerciseService);
//    }



    public List<ExerciseRecordDTO> searchVolumeRecords(ExerciseVolumeRequest request) {
        System.out.println("요청 데이터: " + request);

        // 요청에서 필요한 필드 추출
        String memberId = request.getMemberId();
        List<Long> exerciseIds = request.getExerciseIds();
        Integer exerciseService = request.getExerciseServiceNumber();

        // 최종 결과를 저장할 리스트
        List<ExerciseRecordDTO> result = new ArrayList<>();

        // 각 exerciseId에 대해 처리
        for (Long exerciseId : exerciseIds) {
            // 1. 각 exerciseId의 최근 날짜를 조회
            LocalDate mostRecentDate = exerciseRecordRepository.findMostRecentRecordDateExcludingToday(
                    memberId,
                    exerciseId,
                    exerciseService
            );

            if (mostRecentDate == null) {
                System.out.println("exerciseId: " + exerciseId + "에 대한 오늘 제외 최근 날짜가 없습니다.");
                continue; // 데이터가 없으면 다음 exerciseId로 넘어감
            }

            System.out.println("exerciseId: " + exerciseId + "에 대한 가장 최근 날짜: " + mostRecentDate);

            // 2. 해당 날짜에 대한 데이터 조회
            List<ExerciseRecord> exerciseRecords = exerciseRecordRepository.findRecordsByExerciseIdsAndDate(
                    List.of(exerciseId), // 단일 exerciseId를 리스트로 변환
                    memberId,
                    exerciseService,
                    mostRecentDate
            );

            // 3. 조회된 데이터를 DTO로 변환하여 결과 리스트에 추가
            result.addAll(
                    exerciseRecords.stream()
                            .map(record -> new ExerciseRecordDTO(
                                    record.getExerciseRecordId(),
                                    record.getMember().getMemberId(),
                                    record.getSetNumber(),
                                    new SetDTO(
                                            record.getSet().getCompleted(),
                                            record.getSet().getKg(),
                                            record.getSet().getKm(),
                                            record.getSet().getLbs(),
                                            record.getSet().getMi(),
                                            record.getSet().getReps(),
                                            record.getSet().getTime()
                                    ),
                                    new ExerciseDto(
                                            record.getExercise().getId(),
                                            record.getExercise().getExerciseName(),
                                            record.getExercise().getMainMuscleGroup(),
                                            record.getExercise().getDetailMuscleGroup(),
                                            record.getExercise().getPopularityGroup(),
                                            record.getExercise().getIsLiked()
                                    ),
                                    record.getExerciseServiceNumber().getId(),
                                    record.getExerciseType().getId(),
                                    record.getVolume(),
                                    record.getWeightUnit(),
//                                    record.getKmUnit(),
                                    record.getRecordDate(),
//                                    record.getKmVolume(),
//                                    record.getMiVolume(),
                                    record.getKgVolume()
//                                    record.getLbsVolume(),
//                                    record.getTimeVolume(),
//                                    record.getRepsVolume()
                            ))
                            .toList()
            );
        }

        // 최종 결과 리스트 반환
        return result;
    }


    public List<ExerciseRecordDTO> getExerciseRecordsForDate(String memberId, LocalDate recordDate) {

        System.out.println(memberId);
        System.out.println(recordDate);

        // ExerciseRecord 엔티티를 검색
        List<ExerciseRecord> exerciseRecords = exerciseRepository.findRecordsByMemberIdAndDate(memberId, recordDate);


        // ExerciseRecord 엔티티를 ExerciseRecordDTO로 변환 후 반환
        return exerciseRecords.stream()
                .map(record -> new ExerciseRecordDTO(
                        record.getExerciseRecordId(), // ID 필드 매핑
                        record.getMember().getMemberId(), // Member ID 매핑
                        record.getSetNumber(), // 세트 번호 매핑
                        new SetDTO( // SetDTO 생성
                                record.getSet().getCompleted(), // 완료 여부
                                record.getSet().getKg(),        // 킬로그램
                                record.getSet().getKm(),        // 킬로미터
                                record.getSet().getLbs(),       // 파운드
                                record.getSet().getMi(),        // 마일
                                record.getSet().getReps(),      // 반복 횟수
                                record.getSet().getTime()       // 시간
                        ),
                        new ExerciseDto( // ExerciseDto 생성
                                record.getExercise().getId(), // 운동 ID
                                record.getExercise().getExerciseName(), // 운동 이름
                                record.getExercise().getMainMuscleGroup(), // 주요 근육 그룹
                                record.getExercise().getDetailMuscleGroup(), // 세부 근육 그룹
                                record.getExercise().getPopularityGroup(), // 인기 그룹 여부
                                record.getExercise().getIsLiked() // 좋아요 상태
                        ),
                        record.getExerciseServiceNumber().getId(), // 운동 서비스 ID 매핑
                        record.getExerciseType().getId(), // 운동 타입 ID 매핑
                        record.getVolume(), // 볼륨 매핑
                        record.getWeightUnit(), // 무게 단위 매핑
//                        record.getKmUnit(), // 거리 단위 매핑
                        record.getRecordDate(), // 기록 날짜 매핑
//                        record.getKmVolume(), // km 볼륨 매핑
//                        record.getMiVolume(), // 마일 볼륨 매핑
                        record.getKgVolume() // kg 볼륨 매핑
//                        record.getLbsVolume(), // lbs 볼륨 매핑
//                        record.getTimeVolume(), // 시간 매핑
//                        record.getRepsVolume() // 반복 횟수 매핑
                ))
                .toList();
    }

}
