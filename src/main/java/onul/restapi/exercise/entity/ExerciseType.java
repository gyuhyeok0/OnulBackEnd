package onul.restapi.exercise.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise_type")
public class ExerciseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name; // Exercise Type 이름 (예: REPETITION, DISTANCE 등)

    // 기본 생성자
    public ExerciseType() {}

    // 생성자
    public ExerciseType(String name) {
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
        return "ExerciseType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
