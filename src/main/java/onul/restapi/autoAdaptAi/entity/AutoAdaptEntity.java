package onul.restapi.autoAdaptAi.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // 기존 객체를 빌더로 변경 가능하도록 추가
@Table(name = "auto_adapt") // 테이블명 유지
public class AutoAdaptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ✅ 기존 List<Integer> 대신 Exercise와 ManyToMany 관계 설정
    @ManyToMany
    @JoinTable(
            name = "auto_adapt_exercises", // 중간 테이블 생성
            joinColumns = @JoinColumn(name = "auto_adapt_id"), // AutoAdaptEntity의 ID
            inverseJoinColumns = @JoinColumn(name = "exercise_id") // Exercise의 ID
    )
    private List<Exercise> exercises;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보


    private LocalDate date;


}
