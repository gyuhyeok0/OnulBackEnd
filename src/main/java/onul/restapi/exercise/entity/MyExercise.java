package onul.restapi.exercise.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onul.restapi.member.entity.Members;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@Table(name = "my_exercises")
public class MyExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member;

    @ManyToMany
    @JoinTable(
            name = "my_exercise_details",
            joinColumns = @JoinColumn(name = "my_exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<Exercise> exercises;

    @Column(nullable = false)
    private String muscleGroup;

    public MyExercise(Long id, Members member, List<Exercise> exercises, String muscleGroup) {
        this.id = id;
        this.member = member;
        this.exercises = exercises;
        this.muscleGroup = muscleGroup;
    }

    @Override
    public String toString() {
        return "MyExercise{" +
                "id=" + id +
                ", member=" + member +
                ", exercises=" + exercises +
                ", muscleGroup='" + muscleGroup + '\'' +
                '}';
    }
}
