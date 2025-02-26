package onul.restapi.exercise.repository;

import io.lettuce.core.dynamic.annotation.Param;
import onul.restapi.exercise.dto.ExerciseRecordDTO;
import onul.restapi.exercise.entity.AiExerciseRecordDTO;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.entity.ExerciseRecord;
import onul.restapi.exercise.entity.ExerciseServiceNumber;
import onul.restapi.member.entity.Members;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {

    Optional<ExerciseRecord> findByRecordDateAndExerciseServiceNumberAndSetNumberAndExercise(
            LocalDate recordDate,
            ExerciseServiceNumber exerciseServiceNumber,
            int setNumber,
            Exercise exercise
    );

    //삭제
    Optional<ExerciseRecord> findByMemberAndExerciseAndExerciseServiceNumberAndSetNumberAndRecordDate(
            Members member,
            Exercise exercise,
            ExerciseServiceNumber exerciseServiceNumber,
            int setNumber,
            LocalDate recordDate
    );

    @Query("SELECT DISTINCT er.recordDate FROM ExerciseRecord er " +
            "JOIN er.member m " +
            "JOIN er.exercise e " +
            "JOIN er.exerciseServiceNumber esn " +
            "WHERE m.memberId = :memberId " +
            "AND e.id = :exerciseId " +
            "AND esn.id = :exerciseService")
    List<LocalDate> findDistinctRecordDatesByMemberIdAndExerciseIdAndServiceNumber(
            @Param("memberId") String memberId,
            @Param("exerciseId") Long exerciseId,
            @Param("exerciseService") Integer exerciseService);


    @Query("SELECT DISTINCT er.recordDate FROM ExerciseRecord er " +
            "JOIN er.member m " +
            "JOIN er.exercise e " +
            "JOIN er.exerciseServiceNumber esn " +
            "WHERE m.memberId = :memberId " +
            "AND e.id = :exerciseId " +
            "AND esn.id = :exerciseService " +
            "AND er.recordDate >= :threeMonthsAgo " +
            "ORDER BY er.recordDate DESC")
    List<LocalDate> findRecentRecordDates(
            @Param("memberId") String memberId,
            @Param("exerciseId") Long exerciseId,
            @Param("exerciseService") Integer exerciseService,
            @Param("threeMonthsAgo") LocalDate threeMonthsAgo);



    @Query("SELECT MAX(er.recordDate) FROM ExerciseRecord er " +
            "JOIN er.member m " +
            "JOIN er.exercise e " +
            "JOIN er.exerciseServiceNumber esn " +
            "WHERE m.memberId = :memberId " +
            "AND e.id = :exerciseId " +
            "AND esn.id = :exerciseService " +
            "AND er.recordDate < CURRENT_DATE") // 오늘 이전의 날짜만 조회
    LocalDate findMostRecentRecordDateExcludingToday(
            @Param("memberId") String memberId,
            @Param("exerciseId") Long exerciseId,
            @Param("exerciseService") Integer exerciseService
    );


    @Query("SELECT r FROM ExerciseRecord r " +
            "WHERE r.exercise.id IN :exerciseIds " +
            "AND r.member.memberId = :memberId " +
            "AND r.exerciseServiceNumber.id = :exerciseService " +
            "AND r.recordDate = :recordDate")
    List<ExerciseRecord> findRecordsByExerciseIdsAndDate(
            @Param("exerciseIds") List<Long> exerciseIds,
            @Param("memberId") String memberId,
            @Param("exerciseService") int exerciseService,
            @Param("recordDate") LocalDate recordDate
    );


    boolean existsByMemberAndRecordDate(Members member, LocalDate date);

    @Query("SELECT r FROM ExerciseRecord r WHERE r.member.memberId = :memberId AND r.recordDate > :date")
    List<ExerciseRecord> findRecordsByMemberIdAndAfterDate(@Param("memberId") String memberId, @Param("date") LocalDate date);


    @Query("SELECT r FROM ExerciseRecord r WHERE r.member.memberId = :memberId")
    List<ExerciseRecord> findRecordsByMemberId(@Param("memberId") String memberId);

    @Query("SELECT e.id FROM Exercise e WHERE e.mainMuscleGroup = :mainMuscleGroup")
    Optional<Long> findExerciseIdByMainMuscleGroup(@Param("mainMuscleGroup") String mainMuscleGroup);


    @Query("SELECT e FROM ExerciseRecord e WHERE e.member.memberId = :memberId AND e.recordDate > :oneWeekAgo")
    List<ExerciseRecord> findRecentRecords(@Param("memberId") String memberId, @Param("oneWeekAgo") LocalDate oneWeekAgo);


    List<ExerciseRecord> findByMemberMemberIdAndRecordDateBetween(String memberId, LocalDate oneWeekAgo, LocalDate yesterday);


    @Query("SELECT DISTINCT er.recordDate FROM ExerciseRecord er " +
            "JOIN er.exerciseServiceNumber esn " +
            "WHERE er.member.memberId = :memberId " +
            "AND esn.id = 3 " + // 🔥 exercise_service_id = 3 추가
            "AND er.recordDate < CURRENT_DATE " + // 오늘 제외
            "ORDER BY er.recordDate DESC")
    List<LocalDate> findRecent6Days(@Param("memberId") String memberId, Pageable pageable);


    @Query("SELECT er FROM ExerciseRecord er " +
            "JOIN er.exerciseServiceNumber esn " +
            "WHERE er.member.memberId = :memberId " +
            "AND esn.id = 3 " + // 🔥 exercise_service_id = 3 조건 추가
            "AND er.recordDate IN :recentDates " + // 🔥 최근 6일 날짜만 조회
            "ORDER BY er.recordDate DESC")
    List<ExerciseRecord> findExercisesByRecentDates(
            @Param("memberId") String memberId,
            @Param("recentDates") List<LocalDate> recentDates);


    void deleteByMember(Members member);
}
