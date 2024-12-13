package onul.restapi.exercise.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise_service")
public class ExerciseServiceNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name; // Exercise Service 이름 (예: 맞춤일정, 일반일정 등)

    // 기본 생성자
    public ExerciseServiceNumber() {}

    // 생성자
    public ExerciseServiceNumber(String name) {
        this.name = name;
    }

    // Getter & Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExerciseService{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
