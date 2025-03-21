package onul.restapi.record.service;

import onul.restapi.exercise.repository.ExerciseRecordRepository;
import onul.restapi.management.repository.TotalFoodDataRepository;
import onul.restapi.management.repository.BodyDataRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import onul.restapi.member.service.MemberService;
import onul.restapi.record.dto.MonthDataExistResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecordService {

    private final ExerciseRecordRepository exerciseRecordRepository;
    private final TotalFoodDataRepository totalFoodDataRepository;
    private final BodyDataRepository bodyDataRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public RecordService(ExerciseRecordRepository exerciseRecordRepository, TotalFoodDataRepository totalFoodDataRepository, BodyDataRepository bodyDataRepository, MemberRepository memberRepository, MemberService memberService) {
        this.exerciseRecordRepository = exerciseRecordRepository;
        this.totalFoodDataRepository = totalFoodDataRepository;
        this.bodyDataRepository = bodyDataRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    public MonthDataExistResponse checkMonthDataExist(String memberId, LocalDate month) {

        // ✅ Redis 캐시에서 회원 정보 가져오기
        Members member = memberService.getMemberById(memberId);

        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());

        // ✅ 각 테이블에서 한 달치 데이터를 한 번에 가져오기
        Set<LocalDate> exerciseDates = new HashSet<>(exerciseRecordRepository.findExerciseDates(member, startOfMonth, endOfMonth));
        Set<LocalDate> foodDates = new HashSet<>(totalFoodDataRepository.findFoodDates(member, startOfMonth, endOfMonth));
        Set<LocalDate> bodyDates = new HashSet<>(bodyDataRepository.findBodyDates(member, startOfMonth, endOfMonth));

        List<MonthDataExistResponse.DayDataExist> dayDataExists = new ArrayList<>();

        // ✅ 해당 월의 모든 날짜를 확인하면서 데이터 존재 여부 매칭
        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            boolean exerciseExists = exerciseDates.contains(date);
            boolean foodExists = foodDates.contains(date);
            boolean bodyExists = bodyDates.contains(date);

            dayDataExists.add(new MonthDataExistResponse.DayDataExist(date.toString(), exerciseExists, foodExists, bodyExists));
        }

        return new MonthDataExistResponse(dayDataExists);
    }


}
