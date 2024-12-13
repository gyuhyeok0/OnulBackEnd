package onul.restapi.exercise.service;

import onul.restapi.exercise.dto.ExerciseRecordDTO;
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

@Service
public class ExerciseRecordService {

    private final ExerciseRecordRepository exerciseRecordRepository;
    private final MemberRepository memberRepository;
    private final ExerciseTypeRepository exerciseTypeRepository;
    private final ExerciseServiceRepository exerciseServiceRepository;
    private final ExerciseRepository exerciseRepository;

    public ExerciseRecordService(ExerciseRecordRepository exerciseRecordRepository, MemberRepository memberRepository, ExerciseTypeRepository exerciseTypeRepository, ExerciseServiceRepository exerciseServiceRepository, ExerciseRepository exerciseRepository) {
        this.exerciseRecordRepository = exerciseRecordRepository;
        this.memberRepository = memberRepository;
        this.exerciseTypeRepository = exerciseTypeRepository;
        this.exerciseServiceRepository = exerciseServiceRepository;
        this.exerciseRepository = exerciseRepository;
    }

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
                try {
                    repsVolume = Integer.parseInt(volumeString);
                    parsedVolume = repsVolume;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("REPETITION에 유효하지 않은 volume 값: " + volumeString);
                }
                break;
            case 2: // DISTANCE
                try {
                    parsedVolume = Double.parseDouble(volumeString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("DISTANCE에 유효하지 않은 volume 값: " + volumeString);
                }
                if ("km".equals(exerciseRecord.getKmUnit())) {
                    kmVolume = roundToTwoDecimalPlaces((Double) parsedVolume);
                    miVolume = roundToTwoDecimalPlaces(kmVolume * 0.62);
                } else if ("mi".equals(exerciseRecord.getKmUnit())) {
                    miVolume = roundToTwoDecimalPlaces((Double) parsedVolume);
                    kmVolume = roundToTwoDecimalPlaces(miVolume / 0.62);
                }
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
                timeVolume = volumeString;
                parsedVolume = timeVolume;
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
            ExerciseRecord updatedRecord = ExerciseRecord.builder()
                    .exerciseRecordId(existingRecord.getExerciseRecordId()) // 기존 ID 유지
                    .member(existingRecord.getMember()) // 기존 회원 정보 유지
                    .exerciseServiceNumber(existingRecord.getExerciseServiceNumber()) // 기존 서비스 번호 유지
                    .setNumber(existingRecord.getSetNumber()) // 기존 세트 번호 유지
                    .set(existingRecord.getSet()) // 기존 세트 정보 유지
                    .exercise(existingRecord.getExercise()) // 기존 운동 정보 유지
                    .exerciseType(existingRecord.getExerciseType()) // 기존 운동 타입 유지
                    .volume(volumeString) // 새로운 데이터 설정
                    .repsVolume(repsVolume)
                    .kmVolume(kmVolume)
                    .miVolume(miVolume)
                    .kgVolume(kgVolume)
                    .lbsVolume(lbsVolume)
                    .timeVolume(timeVolume)
                    .recordDate(existingRecord.getRecordDate()) // 기존 기록 날짜 유지
                    .weightUnit(existingRecord.getWeightUnit())
                    .kmUnit(existingRecord.getKmUnit())
                    .build();

            exerciseRecordRepository.save(updatedRecord);
            System.out.println("기존 운동 기록이 업데이트되었습니다: " + updatedRecord.getExerciseRecordId());
        } else {
            // 새로운 데이터 생성 (빌더 사용)
            ExerciseRecord newRecord = ExerciseRecord.builder()
                    .member(member)
                    .exerciseServiceNumber(exerciseServiceNumberEntity)
                    .setNumber(exerciseRecord.getSetNumber())
                    .set(exerciseRecord.getSet())
                    .exercise(exerciseEntity)
                    .exerciseType(exerciseTypeEntity)
                    .volume(volumeString)
                    .repsVolume(repsVolume)
                    .kmVolume(kmVolume)
                    .miVolume(miVolume)
                    .kgVolume(kgVolume)
                    .lbsVolume(lbsVolume)
                    .timeVolume(timeVolume)
                    .recordDate(today)
                    .weightUnit(exerciseRecord.getWeightUnit())
                    .kmUnit(exerciseRecord.getKmUnit())
                    .build();

            exerciseRecordRepository.save(newRecord);
            System.out.println("새로운 운동 기록이 저장되었습니다: " + newRecord.getExerciseRecordId());
        }
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



}
