package onul.restapi.analysis.service;

import onul.restapi.analysis.dto.ExerciseDailyRecord;
import onul.restapi.analysis.dto.ExerciseVolumeResponse;
import onul.restapi.analysis.entity.ExerciseGroupVolumeStatsEntity;
import onul.restapi.analysis.entity.ExerciseVolumeStatsEntity;
import onul.restapi.analysis.entity.MuscleFatigue;
import onul.restapi.analysis.entity.WeightAndDietStatistics;
import onul.restapi.analysis.repository.ExerciseGroupVolumeStatsRepository;
import onul.restapi.analysis.repository.ExerciseVolumeRepository;
import onul.restapi.analysis.repository.MuscleFatigueRepository;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.entity.ExerciseRecord;
import onul.restapi.exercise.repository.ExerciseRecordRepository;
import onul.restapi.exercise.repository.ExerciseRepository;
import onul.restapi.management.entity.BodyDataEntity;
import onul.restapi.management.entity.TotalFoodData;
import onul.restapi.management.repository.BodyDataRepository;
import onul.restapi.management.repository.TotalFoodDataRepository;
import onul.restapi.management.repository.WeightAndDietStatisticsRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.IntStream;


@Service
public class AnalysisService {

    private final ExerciseRecordRepository exerciseRecordRepository;

    private final ExerciseVolumeRepository exerciseVolumeRepository;

    private final MemberRepository memberRepository;

    private final ExerciseRepository exerciseRepository;

    private final BodyDataRepository bodyDataRepository;

    private final TotalFoodDataRepository totalFoodDataRepository;

    private final WeightAndDietStatisticsRepository weightAndDietStatisticsRepository;

    private final MuscleFatigueRepository muscleFatigueRepository;

    private final ExerciseGroupVolumeStatsRepository exerciseGroupVolumeStatsRepository;

    public AnalysisService(ExerciseRecordRepository exerciseRecordRepository, ExerciseVolumeRepository exerciseVolumeRepository, MemberRepository memberRepository, ExerciseRepository exerciseRepository, BodyDataRepository bodyDataRepository, TotalFoodDataRepository totalFoodDataRepository, WeightAndDietStatisticsRepository weightAndDietStatisticsRepository, MuscleFatigueRepository muscleFatigueRepository, ExerciseGroupVolumeStatsRepository exerciseGroupVolumeStatsRepository) {
        this.exerciseRecordRepository = exerciseRecordRepository;
        this.exerciseVolumeRepository = exerciseVolumeRepository;
        this.memberRepository = memberRepository;
        this.exerciseRepository = exerciseRepository;
        this.bodyDataRepository = bodyDataRepository;
        this.totalFoodDataRepository = totalFoodDataRepository;
        this.weightAndDietStatisticsRepository = weightAndDietStatisticsRepository;
        this.muscleFatigueRepository = muscleFatigueRepository;
        this.exerciseGroupVolumeStatsRepository = exerciseGroupVolumeStatsRepository;
    }

    public void updateVolumeStatistics(String memberId) {

        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 1. 기존 저장된 통계 가져오기
        Optional<LocalDate> lastSavedDate = exerciseVolumeRepository.findLatestRecordDateByMemberId(member.getMemberId());

        // 오늘 날짜 계산
        LocalDate today = LocalDate.now();

        // 2. 새 데이터 가져오기 (이전 저장 날짜 이후 데이터만)
        List<ExerciseRecord> newRecords;
        if (lastSavedDate.isPresent()) {
            // 이전 저장 날짜 이후의 데이터 가져오기 (오늘 데이터는 제외)
            newRecords = exerciseRecordRepository.findRecordsByMemberIdAndAfterDate(
                            member.getMemberId(), lastSavedDate.get())
                    .stream()
                    .filter(record -> !record.getRecordDate().isEqual(today)) // 오늘 데이터 제외
                    .collect(Collectors.toList());

        } else {
            // 저장된 통계가 없을 경우 모든 데이터를 가져오기 (오늘 데이터는 제외)
            newRecords = exerciseRecordRepository.findRecordsByMemberId(member.getMemberId())
                    .stream()
                    .filter(record -> !record.getRecordDate().isEqual(today)) // 오늘 데이터 제외
                    .collect(Collectors.toList());
        }

        // 3. 데이터가 없는 경우 처리
        if (newRecords == null || newRecords.isEmpty()) {
            return; // 실행 종료
        }
//
        // 4. 데이터 그룹화 (날짜별)
        Map<LocalDate, Map<Long, Double>> groupedData = groupRecordsByDateAndExercise(newRecords);

        // 5. 근육 그룹별 데이터 계산
        Map<LocalDate, Map<String, Map<String, Map<Long, Double>>>> muscleGroupVolumes = calculateGroupVolumes(groupedData, newRecords);


        // 데이터 처리
        muscleGroupVolumes.forEach((date, mainGroupMap) -> {
            mainGroupMap.forEach((mainMuscleGroup, detailGroupMap) -> {
                detailGroupMap.forEach((detailMuscleGroup, exerciseVolumeMap) -> {
                    exerciseVolumeMap.forEach((exerciseId, totalVolume) -> {
                        // Null 값 체크
                        if (mainMuscleGroup == null || detailMuscleGroup == null) {
                            return;
                        }

                        // Exercise 조회
                        Exercise exercise = exerciseRepository.findById(exerciseId)
                                .orElseThrow(() -> new IllegalArgumentException("운동을 찾을 수 없습니다: exerciseId=" + exerciseId));

                        // 새로운 통계 데이터 저장
                        ExerciseVolumeStatsEntity stats = ExerciseVolumeStatsEntity.builder()
                                .member(member)
                                .recordDate(date)
                                .exercise(exercise)
                                .mainMuscleGroup(mainMuscleGroup)
                                .detailMuscleGroup(detailMuscleGroup)
                                .dailyVolume(totalVolume)
                                .build();

                        exerciseVolumeRepository.save(stats);
                    });
                });
            });
        });

        // 주간 날짜와 월 날짜 배열
        List<LocalDate> weekDates = new ArrayList<>();
        List<LocalDate> monthDates = new ArrayList<>();

        muscleGroupVolumes.forEach((date, value) -> {

            // 해당 주의 시작일 (월요일)
            LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);

            // 해당 월의 첫 번째 날
            LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());

            // 주간 날짜가 이미 배열에 없다면 추가
            if (!weekDates.contains(startOfWeek)) {
                weekDates.add(startOfWeek);
            }

            // 월간 날짜가 이미 배열에 없다면 추가
            if (!monthDates.contains(startOfMonth)) {
                monthDates.add(startOfMonth);
            }
        });

//        // 결과 출력
//        System.out.println("주간 날짜 배열: " + weekDates);
//        System.out.println("월간 날짜 배열: " + monthDates);

        // 주간 및 월간 볼륨 합산 결과를 저장할 Map
        Map<LocalDate, Map<String, Double>> weeklyVolumes = new HashMap<>();
        Map<LocalDate, Map<String, Double>> monthlyVolumes = new HashMap<>();

        // 주간 볼륨 계산
        weekDates.forEach(startOfWeek -> {
            // 해당 주간의 마지막 날짜
            LocalDate endOfWeek = startOfWeek.plusDays(6);

            // 주간 데이터 가져오기
            List<ExerciseVolumeStatsEntity> weeklyStats = exerciseVolumeRepository.findByRecordDateBetween(startOfWeek, endOfWeek);

            // 주간 볼륨 합산
            Map<String, Double> weeklyGroupVolumes = new HashMap<>();
            weeklyStats.forEach(stat -> {
                String muscleGroup = stat.getMainMuscleGroup();
                double volume = stat.getDailyVolume();
                weeklyGroupVolumes.put(muscleGroup, weeklyGroupVolumes.getOrDefault(muscleGroup, 0.0) + volume);
            });

            // 결과 저장
            weeklyVolumes.put(startOfWeek, weeklyGroupVolumes);
        });

        // 월간 볼륨 계산
        monthDates.forEach(startOfMonth -> {
            // 해당 월의 마지막 날짜
            LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

            // 월간 데이터 가져오기
            List<ExerciseVolumeStatsEntity> monthlyStats = exerciseVolumeRepository.findByRecordDateBetween(startOfMonth, endOfMonth);

            // 월간 볼륨 합산
            Map<String, Double> monthlyGroupVolumes = new HashMap<>();
            monthlyStats.forEach(stat -> {
                String muscleGroup = stat.getMainMuscleGroup();
                double volume = stat.getDailyVolume();
                monthlyGroupVolumes.put(muscleGroup, monthlyGroupVolumes.getOrDefault(muscleGroup, 0.0) + volume);
            });

            // 결과 저장
            monthlyVolumes.put(startOfMonth, monthlyGroupVolumes);
        });

        // 주간 볼륨 저장
        weeklyVolumes.forEach((startOfWeek, volumes) -> {
            volumes.forEach((muscleGroup, totalVolume) -> {
                // 기존 데이터 조회
                ExerciseGroupVolumeStatsEntity existingVolume = exerciseGroupVolumeStatsRepository.findByMember_MemberIdAndPeriodTypeAndMainMuscleGroupAndStartDate(
                        member.getMemberId(),
                        "WEEKLY",
                        muscleGroup,
                        startOfWeek
                );

                if (existingVolume != null) {
                    // 기존 데이터가 있으면 업데이트
                    existingVolume.setTotalVolume(totalVolume);
                    exerciseGroupVolumeStatsRepository.save(existingVolume);
                } else {
                    // 기존 데이터가 없으면 새로 저장
                    ExerciseGroupVolumeStatsEntity weeklyVolume = ExerciseGroupVolumeStatsEntity.builder()
                            .startDate(startOfWeek)
                            .periodType("WEEKLY")
                            .mainMuscleGroup(muscleGroup)
                            .totalVolume(totalVolume)
                            .member(member)
                            .build();
                    exerciseGroupVolumeStatsRepository.save(weeklyVolume);
                }
            });
        });

        // 월간 볼륨 저장
        monthlyVolumes.forEach((startOfMonth, volumes) -> {
            volumes.forEach((muscleGroup, totalVolume) -> {
                // 기존 데이터 조회
                ExerciseGroupVolumeStatsEntity existingVolume = exerciseGroupVolumeStatsRepository.findByMember_MemberIdAndPeriodTypeAndMainMuscleGroupAndStartDate(
                        member.getMemberId(),
                        "MONTHLY",
                        muscleGroup,
                        startOfMonth
                );

                if (existingVolume != null) {
                    // 기존 데이터가 있으면 업데이트
                    existingVolume.setTotalVolume(totalVolume);
                    exerciseGroupVolumeStatsRepository.save(existingVolume);
                } else {
                    // 기존 데이터가 없으면 새로 저장
                    ExerciseGroupVolumeStatsEntity monthlyVolume = ExerciseGroupVolumeStatsEntity.builder()
                            .startDate(startOfMonth)
                            .periodType("MONTHLY")
                            .mainMuscleGroup(muscleGroup)
                            .totalVolume(totalVolume)
                            .member(member)
                            .build();
                    exerciseGroupVolumeStatsRepository.save(monthlyVolume);
                }
            });
        });


    }


    private Map<LocalDate, Map<Long, Double>> groupRecordsByDateAndExercise(List<ExerciseRecord> records) {
        return records.stream()
                .filter(record -> record.getExerciseTypeId() != null && record.getExerciseTypeId() == 3) // 중량 운동만 필터링
                .collect(Collectors.groupingBy(
                        ExerciseRecord::getRecordDate, // 날짜별 그룹화
                        Collectors.groupingBy(
                                record -> record.getExercise().getId(), // 운동 ID별 그룹화
                                Collectors.collectingAndThen(
                                        Collectors.toMap(
                                                ExerciseRecord::getExerciseServiceId, // 서비스 ID별 그룹화
                                                ExerciseRecord::getKgVolume, // 볼륨 값 (마지막 값으로 저장)
                                                (first, second) -> second // 동일 서비스 ID의 마지막 값을 선택
                                        ),
                                        serviceVolumeMap -> serviceVolumeMap.values().stream()
                                                .mapToDouble(Double::doubleValue)
                                                .sum() // 서비스 ID별 볼륨을 합산
                                )
                        )
                ));
    }

    private Map<LocalDate, Map<String, Map<String, Map<Long, Double>>>> calculateGroupVolumes(
            Map<LocalDate, Map<Long, Double>> groupedData,
            List<ExerciseRecord> records) {

        // 반환 타입 변경: Map<LocalDate, Map<MainMuscleGroup, Map<DetailMuscleGroup, Map<ExerciseId, Volume>>>>
        Map<LocalDate, Map<String, Map<String, Map<Long, Double>>>> muscleGroupVolumes = new HashMap<>();

        for (Map.Entry<LocalDate, Map<Long, Double>> entry : groupedData.entrySet()) {
            LocalDate date = entry.getKey();
            Map<Long, Double> exerciseVolumes = entry.getValue();

            Map<String, Map<String, Map<Long, Double>>> mainMuscleGroupMap = new HashMap<>();

            for (Map.Entry<Long, Double> exerciseEntry : exerciseVolumes.entrySet()) {
                Long exerciseId = exerciseEntry.getKey();
                Double volume = exerciseEntry.getValue();

                // 해당 exerciseId에 해당하는 ExerciseRecord를 찾음
                ExerciseRecord record = records.stream()
                        .filter(r -> r.getExercise().getId().equals(exerciseId))
                        .findFirst()
                        .orElse(null);

                if (record != null) {
                    String mainMuscleGroup = record.getExercise().getMainMuscleGroup();
                    String detailMuscleGroup = record.getExercise().getDetailMuscleGroup();

                    // MainMuscleGroup 초기화
                    mainMuscleGroupMap.putIfAbsent(mainMuscleGroup, new HashMap<>());
                    Map<String, Map<Long, Double>> detailGroupMap = mainMuscleGroupMap.get(mainMuscleGroup);

                    // DetailMuscleGroup 초기화
                    detailGroupMap.putIfAbsent(detailMuscleGroup, new HashMap<>());
                    Map<Long, Double> exerciseVolumeMap = detailGroupMap.get(detailMuscleGroup);

                    // exerciseId와 볼륨 추가
                    exerciseVolumeMap.put(exerciseId, exerciseVolumeMap.getOrDefault(exerciseId, 0.0) + volume);
                }
            }

            muscleGroupVolumes.put(date, mainMuscleGroupMap);
        }

        return muscleGroupVolumes;
    }




    public void updateWeightAndDietStatistics(String memberId) {

        // 1. 회원 정보 가져오기
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 2. 이전 달의 첫 번째 날과 이번 달의 마지막 날 계산
        LocalDate today = LocalDate.now();
        // 이전 달의 첫 번째 날
        LocalDate monthStart = today.minusMonths(1).withDayOfMonth(1);

        // 이번 달의 첫째 날
        LocalDate currentMonthStart = today.withDayOfMonth(1);

        // 이번 달의 마지막 날
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());

        // 3. 이전 달의 BodyData와 FoodData 가져오기
        List<BodyDataEntity> bodyDataList = bodyDataRepository.findByMemberAndDateBetween(member, monthStart, monthEnd);
        List<TotalFoodData> foodDataList = totalFoodDataRepository.findByMemberAndDateBetween(member, monthStart, monthEnd);


        // 평균값 계산 함수 (소수점 2째 자리로 반올림)
        java.util.function.DoubleUnaryOperator roundToTwoDecimalPlaces = value ->
                BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();

        double averageWeight = roundToTwoDecimalPlaces.applyAsDouble(bodyDataList.stream()
                .filter(data -> data.getWeight() != null && data.getWeight() != 0.0)
                .mapToDouble(BodyDataEntity::getWeight)
                .average()
                .orElse(0.0));

        double averageBodyFatMass = roundToTwoDecimalPlaces.applyAsDouble(bodyDataList.stream()
                .filter(data -> data.getBodyFatMass() != null && data.getBodyFatMass() != 0.0)
                .mapToDouble(BodyDataEntity::getBodyFatMass)
                .average()
                .orElse(0.0));

        double averageBodyFatPercentage = roundToTwoDecimalPlaces.applyAsDouble(bodyDataList.stream()
                .filter(data -> data.getBodyFatPercentage() != null && data.getBodyFatPercentage() != 0.0)
                .mapToDouble(BodyDataEntity::getBodyFatPercentage)
                .average()
                .orElse(0.0));

        double averageSkeletalMuscleMass = roundToTwoDecimalPlaces.applyAsDouble(bodyDataList.stream()
                .filter(data -> data.getSkeletalMuscleMass() != null && data.getSkeletalMuscleMass() != 0.0)
                .mapToDouble(BodyDataEntity::getSkeletalMuscleMass)
                .average()
                .orElse(0.0));

        double averageCalories = roundToTwoDecimalPlaces.applyAsDouble(foodDataList.stream()
                .map(TotalFoodData::getTotalNutrition)
                .filter(nutrition -> nutrition.containsKey("kcal") && nutrition.get("kcal") != 0.0)
                .mapToDouble(nutrition -> nutrition.get("kcal"))
                .average()
                .orElse(0.0));

        double averageProtein = roundToTwoDecimalPlaces.applyAsDouble(foodDataList.stream()
                .map(TotalFoodData::getTotalNutrition)
                .filter(nutrition -> nutrition.containsKey("protein") && nutrition.get("protein") != 0.0)
                .mapToDouble(nutrition -> nutrition.get("protein"))
                .average()
                .orElse(0.0));

        double averageCarbohydrates = roundToTwoDecimalPlaces.applyAsDouble(foodDataList.stream()
                .map(TotalFoodData::getTotalNutrition)
                .filter(nutrition -> nutrition.containsKey("carbs") && nutrition.get("carbs") != 0.0)
                .mapToDouble(nutrition -> nutrition.get("carbs"))
                .average()
                .orElse(0.0));


        // 통계 업데이트 또는 새로 생성
        WeightAndDietStatistics existingStatistics = weightAndDietStatisticsRepository.findByMemberAndDate(member, currentMonthStart);

        if (existingStatistics != null) {
            // 업데이트
            existingStatistics.setAverageWeight(averageWeight);
            existingStatistics.setAverageBodyFatMass(averageBodyFatMass);
            existingStatistics.setAverageBodyFatPercentage(averageBodyFatPercentage);
            existingStatistics.setAverageSkeletalMuscleMass(averageSkeletalMuscleMass);
            existingStatistics.setAverageCalories(averageCalories);
            existingStatistics.setAverageProtein(averageProtein);
            existingStatistics.setAverageCarbohydrates(averageCarbohydrates);

            weightAndDietStatisticsRepository.save(existingStatistics);
        } else {
            // 새로 생성
            WeightAndDietStatistics newStatistics = WeightAndDietStatistics.builder()
                    .member(member)
                    .date(currentMonthStart) // 이번 달의 첫째 날로 설정
                    .averageWeight(averageWeight)
                    .averageBodyFatMass(averageBodyFatMass)
                    .averageBodyFatPercentage(averageBodyFatPercentage)
                    .averageSkeletalMuscleMass(averageSkeletalMuscleMass)
                    .averageCalories(averageCalories)
                    .averageProtein(averageProtein)
                    .averageCarbohydrates(averageCarbohydrates)
                    .build();

            weightAndDietStatisticsRepository.save(newStatistics);
        }
    }


    public void updateMuscleFatigue(String memberId) {
        // 1. 회원 정보 가져오기
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 2. 최근 운동 기록 가져오기 (오늘 날짜 제외)
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        LocalDate yesterday = LocalDate.now().minusDays(1); // 어제 날짜

        List<ExerciseRecord> recentRecords = exerciseRecordRepository
                .findByMemberMemberIdAndRecordDateBetween(memberId, oneWeekAgo, yesterday);


        if (recentRecords.isEmpty()) {
            throw new IllegalArgumentException("최근 1주일 간 운동 기록이 없습니다.");
        }

        // 3. 운동별로 마지막 세트만 추출
        Map<String, ExerciseRecord> lastSetPerExercise = new HashMap<>();

        for (ExerciseRecord record : recentRecords) {
            String exerciseName = record.getExercise().getExerciseName();

            // 동일한 운동 이름에 대해 setNumber가 가장 큰 값을 저장
            if (!lastSetPerExercise.containsKey(exerciseName) ||
                    lastSetPerExercise.get(exerciseName).getSetNumber() < record.getSetNumber()) {
                lastSetPerExercise.put(exerciseName, record);
            }
        }

        // 4. 날짜별 근육 그룹 볼륨 계산
        Map<LocalDate, Map<String, Double>> dailyMuscleGroupVolume = new HashMap<>();

        for (ExerciseRecord record : lastSetPerExercise.values()) {
            LocalDate recordDate = record.getRecordDate();
            String muscleGroup = record.getExercise().getDetailMuscleGroup();
            double volume = record.getKgVolume() != null ? record.getKgVolume() : 0.0;

            // 날짜별 볼륨 맵 가져오기 (없으면 생성)
            Map<String, Double> muscleGroupVolume = dailyMuscleGroupVolume.getOrDefault(recordDate, new HashMap<>());

            // 근육 그룹별 볼륨 계산
            muscleGroupVolume.put(
                    muscleGroup,
                    muscleGroupVolume.getOrDefault(muscleGroup, 0.0) + volume
            );

            // 갱신된 맵 저장
            dailyMuscleGroupVolume.put(recordDate, muscleGroupVolume);
        }


        // 5. 근육 그룹별 회복 시간 정의
        Map<String, Integer> recoveryTimeByMuscleGroup = getRecoveryTimeByMuscleGroup();

        // 6. 날짜별 근육 그룹 피로도 계산
        Map<LocalDate, Map<String, Double>> dailyMuscleFatigue = new HashMap<>();
        DecimalFormat df = new DecimalFormat("0.00"); // 소숫점 2자리로 포맷 지정

        for (Map.Entry<LocalDate, Map<String, Double>> entry : dailyMuscleGroupVolume.entrySet()) {
            LocalDate date = entry.getKey();
            Map<String, Double> muscleGroupVolume = entry.getValue();

            Map<String, Double> muscleFatigue = new HashMap<>();

            for (Map.Entry<String, Double> muscleEntry : muscleGroupVolume.entrySet()) {
                String muscleGroup = muscleEntry.getKey();
                double volume = muscleEntry.getValue();

                // 회복 시간 가져오기 (기본값: 48시간)
                int recoveryTime = recoveryTimeByMuscleGroup.getOrDefault(muscleGroup, 48);

                // 피로도 계산: 볼륨 × (1 ÷ 회복 시간)
                double fatigue = volume * (1.0 / recoveryTime);

                muscleFatigue.put(muscleGroup, fatigue);
            }

            dailyMuscleFatigue.put(date, muscleFatigue);
        }


        // 7. 근육 그룹별 주간 평균 피로도 계산
        Map<String, Double> weeklyAverageFatigue = new HashMap<>();
        Map<String, List<Double>> muscleFatigueValues = new HashMap<>();

        // 모든 날짜의 피로도 데이터를 근육 그룹별로 모으기
        dailyMuscleFatigue.forEach((date, muscleFatigueMap) -> {
            muscleFatigueMap.forEach((muscleGroup, fatigue) -> {
                muscleFatigueValues.putIfAbsent(muscleGroup, new ArrayList<>());
                muscleFatigueValues.get(muscleGroup).add(fatigue);
            });
        });

        // 평균 피로도 계산
        muscleFatigueValues.forEach((muscleGroup, fatigueValues) -> {
            double averageFatigue = fatigueValues.stream()
                    .collect(Collectors.averagingDouble(Double::doubleValue));
            weeklyAverageFatigue.put(muscleGroup, averageFatigue);
        });


        // 8. MuscleFatigue 엔티티 저장
        LocalDate today = LocalDate.now(); // 오늘 날짜
        List<MuscleFatigue> muscleFatigueList = new ArrayList<>();

        weeklyAverageFatigue.forEach((muscleGroup, averageFatigue) -> {
            // 존재 여부 확인
            MuscleFatigue existingRecord = muscleFatigueRepository.findByMemberMemberIdAndMuscleGroupAndCalculationDate(
                    member.getMemberId(), muscleGroup, today);

            if (existingRecord != null) {
                // 기존 데이터 업데이트
                existingRecord.setFatigueScore(BigDecimal.valueOf(averageFatigue)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue());
                muscleFatigueRepository.save(existingRecord);
            } else {
                // 새 데이터 삽입
                MuscleFatigue muscleFatigue = MuscleFatigue.builder()
                        .member(member)
                        .muscleGroup(muscleGroup)
                        .fatigueScore(BigDecimal.valueOf(averageFatigue)
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue())
                        .calculationDate(today)
                        .build();

                muscleFatigueList.add(muscleFatigue);
            }
        });

        // Batch 저장 (새로운 데이터만)
        if (!muscleFatigueList.isEmpty()) {
            muscleFatigueRepository.saveAll(muscleFatigueList);
        }



    }


    private Map<String, Integer> getRecoveryTimeByMuscleGroup() {
        Map<String, Integer> recoveryTimeMap = new HashMap<>();
        recoveryTimeMap.put("광배근", 36); // 큰 등 근육
        recoveryTimeMap.put("둔근", 24); // 엉덩이 근육
        recoveryTimeMap.put("대퇴사두근", 24); // 허벅지 앞쪽 큰 근육
        recoveryTimeMap.put("심폐 지구력", 12); // 심폐 지구력 관련 (빠른 회복)
        recoveryTimeMap.put("코어", 24); // 복부 중심 근육
        recoveryTimeMap.put("복직근", 24); // 복부 근육
        recoveryTimeMap.put("외측 대퇴사두근", 24); // 허벅지 외측
        recoveryTimeMap.put("내측 대퇴사두근", 24); // 허벅지 내측
        recoveryTimeMap.put("하부 등", 36); // 하부 등 근육
        recoveryTimeMap.put("하부 가슴", 48); // 하부 가슴 근육
        recoveryTimeMap.put("중간 가슴", 48); // 중간 가슴 근육
        recoveryTimeMap.put("윗가슴", 48); // 상부 가슴 근육
        recoveryTimeMap.put("이두근", 48); // 팔 앞쪽 근육
        recoveryTimeMap.put("전완근", 48); // 팔뚝 근육
        recoveryTimeMap.put("종아리", 24); // 다리 종아리
        recoveryTimeMap.put("전면 삼각근", 36); // 어깨 앞쪽
        recoveryTimeMap.put("승모근", 36); // 목과 등 상부 근육
        recoveryTimeMap.put("삼두근", 48); // 팔 뒤쪽 근육
        recoveryTimeMap.put("후면 삼각근", 36); // 어깨 뒤쪽
        recoveryTimeMap.put("햄스트링", 36); // 허벅지 뒤쪽 큰 근육
        recoveryTimeMap.put("측면 삼각근", 36); // 어깨 옆쪽
        recoveryTimeMap.put("하복부", 24); // 복부 아래쪽
        recoveryTimeMap.put("측면 복근", 24); // 옆구리 근육
        recoveryTimeMap.put("전신 지구력", 12); // 전신 운동 (빠른 회복)
        recoveryTimeMap.put("전신 근력", 24); // 전신 근력 운동
        recoveryTimeMap.put("유연성", 12); // 유연성 관련 (빠른 회복)
        recoveryTimeMap.put("가슴", 48); // 전체 가슴 근육
        recoveryTimeMap.put("어깨", 36); // 전체 어깨 근육
        recoveryTimeMap.put("등", 36); // 전체 등 근육
        recoveryTimeMap.put("회복", 12); // 회복 관련 운동
        recoveryTimeMap.put("상부 가슴", 48); // 가슴 상부 근육
        recoveryTimeMap.put("회전근개", 36); // 어깨 관절 안정 근육
        recoveryTimeMap.put("대흉근", 48); // 가슴 주요 근육
        return recoveryTimeMap;
    }

    public ExerciseVolumeResponse getExerciseVolume(String memberId, LocalDate startDate, LocalDate endDate) {
        // 1️⃣ startDate ~ endDate 기간 동안 운동 데이터 조회
        List<ExerciseVolumeStatsEntity> records = exerciseVolumeRepository
                .findByMember_MemberIdAndRecordDateBetween(memberId, startDate, endDate);

        // 2️⃣ 모든 날짜 리스트 생성 (startDate ~ endDate)
        List<String> allDates = IntStream.rangeClosed(0, (int) ChronoUnit.DAYS.between(startDate, endDate))
                .mapToObj(i -> startDate.plusDays(i).toString())
                .collect(Collectors.toList());

        // 3️⃣ 데이터를 mainMuscleGroup, detailMuscleGroup 별로 그룹화하여 날짜별 볼륨 저장
        Map<String, Map<String, Map<Object, Double>>> groupedData = records.stream()
                .collect(Collectors.groupingBy(
                        ExerciseVolumeStatsEntity::getMainMuscleGroup, // mainMuscleGroup 기준 그룹화
                        Collectors.groupingBy(
                                ExerciseVolumeStatsEntity::getDetailMuscleGroup, // detailMuscleGroup 기준 그룹화
                                Collectors.toMap(
                                        record -> record.getRecordDate().toString(), // 날짜 기준으로 Map 생성
                                        ExerciseVolumeStatsEntity::getDailyVolume, // 볼륨 값 저장
                                        (existing, replacement) -> existing // 중복된 날짜는 기존 값 유지
                                )
                        )
                ));

        System.out.println("그룹데이터"+groupedData);

        // 4️⃣ `startDate` 데이터가 없으면, `startDate` 이전의 가장 최근 데이터를 가져옴
        for (String mainMuscle : groupedData.keySet()) {
            Map<String, Map<Object, Double>> detailGroupMap = groupedData.get(mainMuscle);

            for (String detailMuscle : detailGroupMap.keySet()) {
                Map<Object, Double> dateToVolumeMap = detailGroupMap.get(detailMuscle);

                // `startDate`에 데이터가 없으면, 과거의 가장 최신 데이터를 가져옴
                if (!dateToVolumeMap.containsKey(startDate.toString())) {
                    System.out.println("    ❌ startDate (" + startDate + ") 없음! 과거 데이터 조회...");

                    Double lastVolume = findLatestPastVolume(memberId, detailMuscle, startDate);
                    dateToVolumeMap.put(startDate.toString(), lastVolume);

                    System.out.println("    ✅ 추가된 값: " + startDate + " = " + lastVolume);
                } else {
                    System.out.println("    ✅ startDate (" + startDate + ") 이미 존재, 업데이트 필요 없음.");
                }
            }
        }

        // 5️⃣ 최종 응답을 위한 변환: main과 detail로 키를 구분하여 날짜별 볼륨을 리스트로 변환
        Map<String, List<Double>> formattedRecords = new LinkedHashMap<>();
        for (String mainMuscle : groupedData.keySet()) {
            Map<String, Map<Object, Double>> detailGroupMap = groupedData.get(mainMuscle);

            for (String detailMuscle : detailGroupMap.keySet()) {
                Map<Object, Double> dateToVolumeMap = detailGroupMap.get(detailMuscle);

                // volumes 리스트 초기화
                List<Double> volumes = new ArrayList<>();

                // 모든 날짜에 대해 처리
                for (int i = 0; i < allDates.size(); i++) {
                    String date = allDates.get(i);

                    if (date.equals(startDate.toString()) && !dateToVolumeMap.containsKey(date)) {
                        // startDate에 값이 없을 때만 0.0을 추가
                        System.out.println("    ❌ startDate (" + startDate + ") 없음! 0.0 추가");
                        volumes.add(0.0);
                    } else {
                        Double volume = dateToVolumeMap.get(date);
                        if (volume == null) {
                            // 데이터가 없으면 null로 설정
                            System.out.println("    ⚠️ 날짜: " + date + " → 데이터 없음 (null)");
                            volumes.add(null);
                        } else {
                            // volume이 존재하면 그대로 추가
                            System.out.println("    ✅ 날짜: " + date + " → 데이터 존재 (" + volume + ")");
                            volumes.add(volume);
                        }
                    }
                }

                // main과 detail을 결합하여 키를 사용
                formattedRecords.put("main=" + mainMuscle + ", detail=" + detailMuscle, volumes);
            }
        }

        return new ExerciseVolumeResponse(allDates, formattedRecords);
    }

    /**
     * `startDate` 이전의 가장 최근 운동량을 가져오는 함수
     */
    private Double findLatestPastVolume(String memberId, String detailMuscle, LocalDate startDate) {
        ExerciseVolumeStatsEntity latestRecord = (ExerciseVolumeStatsEntity) exerciseVolumeRepository
                .findTopByMember_MemberIdAndDetailMuscleGroupAndRecordDateBeforeOrderByRecordDateDesc(
                        memberId, detailMuscle, startDate
                ).orElse(null);

        return (latestRecord != null) ? latestRecord.getDailyVolume() : 0.0;
    }

}
