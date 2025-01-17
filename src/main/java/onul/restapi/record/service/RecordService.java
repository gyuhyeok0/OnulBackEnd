package onul.restapi.record.service;

import onul.restapi.exercise.repository.ExerciseRecordRepository;
import onul.restapi.management.repository.TotalFoodDataRepository;
import onul.restapi.management.repository.BodyDataRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import onul.restapi.record.dto.MonthDataExistResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {

    private final ExerciseRecordRepository exerciseRecordRepository;
    private final TotalFoodDataRepository totalFoodDataRepository;
    private final BodyDataRepository bodyDataRepository;
    private final MemberRepository memberRepository;

    public RecordService(ExerciseRecordRepository exerciseRecordRepository, TotalFoodDataRepository totalFoodDataRepository, BodyDataRepository bodyDataRepository, MemberRepository memberRepository) {
        this.exerciseRecordRepository = exerciseRecordRepository;
        this.totalFoodDataRepository = totalFoodDataRepository;
        this.bodyDataRepository = bodyDataRepository;
        this.memberRepository = memberRepository;
    }

    // 운동, 식단, 신체 데이터 존재 여부 체크
    public MonthDataExistResponse checkMonthDataExist(String memberId, LocalDate mountMonth) {

        // Member 엔티티 조회
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));

        List<MonthDataExistResponse.DayDataExist> dayDataExists = new ArrayList<>();

        // 해당 월의 첫 날부터 마지막 날까지 반복하여 데이터 존재 여부 확인
        LocalDate startOfMonth = mountMonth.withDayOfMonth(1);
        LocalDate endOfMonth = mountMonth.withDayOfMonth(mountMonth.lengthOfMonth());

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            boolean exerciseDataExists = isExerciseDataExist(member, date);  // member 객체를 넘김
            boolean foodDataExists = isFoodDataExist(member, date);          // member 객체를 넘김
            boolean bodyDataExists = isBodyDataExist(member, date);          // member 객체를 넘김

            dayDataExists.add(new MonthDataExistResponse.DayDataExist(
                    date.toString(), exerciseDataExists, foodDataExists, bodyDataExists));
        }

        return new MonthDataExistResponse(dayDataExists);
    }

    // 운동 데이터 존재 여부 체크
    private boolean isExerciseDataExist(Members member, LocalDate date) {
        return exerciseRecordRepository.existsByMemberAndRecordDate(member, date); // member 객체를 사용
    }

    // 식단 데이터 존재 여부 체크
    private boolean isFoodDataExist(Members member, LocalDate date) {
        return totalFoodDataRepository.existsByMemberAndDate(member, date); // member 객체를 사용
    }

    // 신체 데이터 존재 여부 체크
    private boolean isBodyDataExist(Members member, LocalDate date) {
        return bodyDataRepository.existsByMemberAndDate(member, date); // member 객체를 사용
    }
}
