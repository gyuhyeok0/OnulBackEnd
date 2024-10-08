package onul.restapi.exercise.service;

import jakarta.transaction.Transactional;
import onul.restapi.exercise.dto.MyExerciseDTO;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.entity.MyExercise;
import onul.restapi.exercise.repository.ExerciseRepository;
import onul.restapi.exercise.repository.MyExerciseRepository;
import onul.restapi.member.entity.Members; // Members 엔티티 임포트
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyExerciseService {

    private final MyExerciseRepository myExerciseRepository;
    private final ExerciseRepository exerciseRepository;
    private final MemberRepository memberRepository;

    public MyExerciseService(MyExerciseRepository myExerciseRepository, ExerciseRepository exerciseRepository, MemberRepository memberRepository) {
        this.myExerciseRepository = myExerciseRepository;
        this.exerciseRepository = exerciseRepository;
        this.memberRepository = memberRepository;
    }




    @Transactional
    public MyExercise registerMyExercises(MyExerciseDTO myExerciseDTO) {
        // 회원 정보 가져오기
        Members member = memberRepository.findById(myExerciseDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        // MyExercise 엔티티를 member_id와 muscle_group으로 조회
        MyExercise existingMyExercise = myExerciseRepository.findByMemberAndMuscleGroup(member, myExerciseDTO.getMuscleGroup());

        // 운동 목록 가져오기 (Exercise ID 리스트)
        List<Exercise> exercises = exerciseRepository.findAllById(myExerciseDTO.getExerciseIds());

        if (exercises.isEmpty()) {
            throw new RuntimeException("존재하지 않는 운동이 포함되어 있습니다.");
        }

        if (existingMyExercise != null) {
            // 기존 운동 목록에 새로운 운동을 추가
            List<Exercise> updatedExercises = new ArrayList<>(existingMyExercise.getExercises());

            for (Exercise exercise : exercises) {
                if (!updatedExercises.contains(exercise)) {
                    updatedExercises.add(exercise);
                }
            }

            // 빌더를 사용하여 기존 엔티티 업데이트 (immutable 방식)
            MyExercise updatedMyExercise = MyExercise.builder()
                    .id(existingMyExercise.getId()) // 기존 ID 유지
                    .member(existingMyExercise.getMember()) // 기존 회원 정보 유지
                    .exercises(updatedExercises) // 업데이트된 운동 목록
                    .muscleGroup(existingMyExercise.getMuscleGroup()) // 기존 근육 그룹 유지
                    .build();

            return myExerciseRepository.save(updatedMyExercise);
        } else {
            // 엔티티가 없는 경우 새로 생성
            MyExercise myExercise = MyExercise.builder()
                    .member(member)
                    .exercises(exercises) // 운동 목록 설정
                    .muscleGroup(myExerciseDTO.getMuscleGroup()) // 근육 그룹 설정
                    .build();

            // 운동 스케줄 저장
            return myExerciseRepository.save(myExercise);
        }
    }



    @Transactional
    public void deleteMyExercise(Long exerciseId, String memberId, String muscleGroup) {
        // 회원 정보 가져오기
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        // 운동 정보 가져오기
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("운동이 존재하지 않습니다."));

        // MyExercise 엔티티를 member_id와 muscle_group으로 조회
        MyExercise existingMyExercise = myExerciseRepository.findByMemberAndMuscleGroup(member, muscleGroup);

        if (existingMyExercise == null) {
            throw new RuntimeException("해당 회원의 근육 그룹에 등록된 운동이 없습니다.");
        }

        // 운동 목록에서 해당 운동 제거
        List<Exercise> updatedExercises = new ArrayList<>(existingMyExercise.getExercises());
        boolean removed = updatedExercises.remove(exercise);

        if (!removed) {
            throw new RuntimeException("해당 운동은 등록되지 않았습니다.");
        }

        // 운동 목록이 비어 있는 경우 MyExercise 엔티티 삭제
        if (updatedExercises.isEmpty()) {
            myExerciseRepository.delete(existingMyExercise);
        } else {
            // 운동 목록이 남아 있는 경우 업데이트하여 저장
            MyExercise updatedMyExercise = MyExercise.builder()
                    .id(existingMyExercise.getId())
                    .member(existingMyExercise.getMember())
                    .exercises(updatedExercises)
                    .muscleGroup(existingMyExercise.getMuscleGroup())
                    .build();

            myExerciseRepository.save(updatedMyExercise);
        }
    }

    // 내 운동 조회
    public List<Exercise> getMyExercises(String memberId, String muscleGroup) {
        System.out.println("Member ID: " + memberId + ", Muscle Group: " + muscleGroup);

        // 회원 정보 가져오기
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        // MyExercise 엔티티를 member_id와 muscle_group으로 조회
        MyExercise existingMyExercise = myExerciseRepository.findByMemberAndMuscleGroup(member, muscleGroup);

        if (existingMyExercise == null) {
            // MyExercise 엔티티가 없는 경우 빈 리스트 반환
            return Collections.emptyList();
        }

        // MyExercise에서 Exercise 목록을 가져옴
        List<Exercise> exercises = existingMyExercise.getExercises();

        if (exercises == null || exercises.isEmpty()) {
            // 운동 정보가 없는 경우 빈 리스트 반환
            return Collections.emptyList();
        }

        return exercises;
    }

}
