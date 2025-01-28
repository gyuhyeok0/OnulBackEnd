package onul.restapi.analysis.repository;

import onul.restapi.analysis.entity.MuscleFatigue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MuscleFatigueRepository extends JpaRepository<MuscleFatigue, Long> {

    // 특정 회원의 근육 피로도 데이터를 날짜 기준으로 조회
    List<MuscleFatigue> findByMemberMemberIdAndCalculationDateBetween(String memberId, LocalDate startDate, LocalDate endDate);

    // 특정 회원의 모든 근육 피로도 데이터 조회
    List<MuscleFatigue> findByMemberMemberId(String memberId);

    // 특정 회원의 특정 날짜의 근육 피로도 데이터 조회
    List<MuscleFatigue> findByMemberMemberIdAndCalculationDate(String memberId, LocalDate calculationDate);

    MuscleFatigue findByMemberMemberIdAndMuscleGroupAndCalculationDate(String memberId, String muscleGroup, LocalDate today);

}
