package onul.restapi.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;

@Entity
@Table(
        name = "muscle_fatigue",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_member_muscle_date",
                columnNames = {"member_id", "muscleGroup", "calculationDate"}
        )
)@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString
public class MuscleFatigue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Members member;

    @Column(nullable = false)
    private String muscleGroup;

    @Column(nullable = false)
    private double fatigueScore;

    @Column(nullable = false)
    private LocalDate calculationDate;


}
