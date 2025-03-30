package onul.restapi.member.service;

import onul.restapi.analysis.entity.MemberLastLogin;
import onul.restapi.analysis.repository.ExerciseGroupVolumeStatsRepository;
import onul.restapi.analysis.repository.ExerciseVolumeRepository;
import onul.restapi.analysis.repository.MemberLastLoginRepository;
import onul.restapi.analysis.repository.MuscleFatigueRepository;
import onul.restapi.autoAdaptAi.repository.AutoAdaptRepository;
import onul.restapi.autoAdaptAi.repository.AutoAdaptSettingRepository;
import onul.restapi.awssns.repository.CodeRepository;
import onul.restapi.exercise.repository.*;
import onul.restapi.inquiry.repository.InquiryRepository;
import onul.restapi.inspection.repository.InspectionRepository;
import onul.restapi.management.repository.*;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import onul.restapi.member.repository.SignupMember;
import onul.restapi.onboarding.rapository.OnboardingRepository;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class DeleteAccountService {

    // 엔티티에 Members
    private final ExerciseGroupVolumeStatsRepository exerciseGroupVolumeStatsRepository;
    private final ExerciseVolumeRepository exerciseVolumeRepository;
    private final MuscleFatigueRepository muscleFatigueRepository;

    private final AutoAdaptRepository autoAdaptRepository;
    private final AutoAdaptSettingRepository autoAdaptSettingRepository;

    private final ExerciseRecordRepository exerciseRecordRepository;
    private final IntensityRepository intensityRepository;
    private final MyExerciseRepository myExerciseRepository;
    private final ScheduleRepository scheduleRepository;

    private final BodyDataRepository bodyDataRepository;
    private final FoodEntityRepository foodEntityRepository;
    private final FoodItemEntityRepository foodItemEntityRepository;
    private final TotalFoodDataRepository totalFoodDataRepository;
    private final WeightAndDietStatisticsRepository weightAndDietStatisticsRepository;

    private final OnboardingRepository onboardingRepository;

    //엔티티에 memberId
    private final MemberRepository memberRepository;
    private final SignupMember signupMember;
    private final InquiryRepository inquiryRepository;
    private final MemberService memberService;
    private final MemberLastLoginRepository memberLastLoginRepository;

    public DeleteAccountService(ExerciseGroupVolumeStatsRepository exerciseGroupVolumeStatsRepository, ExerciseVolumeRepository exerciseVolumeRepository, MuscleFatigueRepository muscleFatigueRepository, AutoAdaptRepository autoAdaptRepository, AutoAdaptSettingRepository autoAdaptSettingRepository, ExerciseRecordRepository exerciseRecordRepository, IntensityRepository intensityRepository, MyExerciseRepository myExerciseRepository, ScheduleRepository scheduleRepository, BodyDataRepository bodyDataRepository, FoodEntityRepository foodEntityRepository, FoodItemEntityRepository foodItemEntityRepository, TotalFoodDataRepository totalFoodDataRepository, WeightAndDietStatisticsRepository weightAndDietStatisticsRepository, OnboardingRepository onboardingRepository, MemberRepository memberRepository, SignupMember signupMember, InquiryRepository inquiryRepository, MemberService memberService, MemberLastLoginRepository memberLastLoginRepository) {
        this.exerciseGroupVolumeStatsRepository = exerciseGroupVolumeStatsRepository;
        this.exerciseVolumeRepository = exerciseVolumeRepository;
        this.muscleFatigueRepository = muscleFatigueRepository;
        this.autoAdaptRepository = autoAdaptRepository;
        this.autoAdaptSettingRepository = autoAdaptSettingRepository;
        this.exerciseRecordRepository = exerciseRecordRepository;
        this.intensityRepository = intensityRepository;
        this.myExerciseRepository = myExerciseRepository;
        this.scheduleRepository = scheduleRepository;
        this.bodyDataRepository = bodyDataRepository;
        this.foodEntityRepository = foodEntityRepository;
        this.foodItemEntityRepository = foodItemEntityRepository;
        this.totalFoodDataRepository = totalFoodDataRepository;
        this.weightAndDietStatisticsRepository = weightAndDietStatisticsRepository;
        this.onboardingRepository = onboardingRepository;
        this.memberRepository = memberRepository;
        this.signupMember = signupMember;
        this.inquiryRepository = inquiryRepository;
        this.memberService = memberService;
        this.memberLastLoginRepository = memberLastLoginRepository;
    }


    @Async("withdrawExecutor") // 💥 여기 추가!
    @Transactional
    public void deleteAccount(String memberId) {

        // 🔥 1. 회원 존재 여부 확인
        Members member = memberService.getMemberById(memberId);

        // 🔥 2. `memberId`가 포함된 테이블 삭제
        inquiryRepository.deleteByMemberId(memberId);
//
//        // 🔥 3. `Members`를 참조하는 테이블 삭제
        exerciseGroupVolumeStatsRepository.deleteByMember(member);

        exerciseVolumeRepository.deleteByMember(member);
        muscleFatigueRepository.deleteByMember(member);
        autoAdaptRepository.deleteByMember(member);
        autoAdaptSettingRepository.deleteByMember(member);
        exerciseRecordRepository.deleteByMember(member);
        intensityRepository.deleteByMember(member);
        myExerciseRepository.deleteByMember(member);
        scheduleRepository.deleteByMember(member);
        bodyDataRepository.deleteByMember(member);
        foodEntityRepository.deleteByMember(member);
        foodItemEntityRepository.deleteByMember(member);
        totalFoodDataRepository.deleteByMember(member);
        weightAndDietStatisticsRepository.deleteByMember(member);
        onboardingRepository.deleteByMember(member);
        memberLastLoginRepository.deleteByMember(member);

        // 🔥 4. 최종적으로 회원 삭제
        memberRepository.deleteById(memberId);

    }



}
