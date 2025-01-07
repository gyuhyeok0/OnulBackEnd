package onul.restapi.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onul.restapi.member.entity.Members;

import java.util.List;

@Entity
@Table(name = "food_recipes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보

    @Column(nullable = false)
    private String recipeId; // 레시피 ID

    @Column(nullable = false)
    private String recipeName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "foodEntity")
    private List<FoodItemEntity> foodItems;


}
