package onul.restapi.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onul.restapi.member.entity.Members;

@Entity
@Table(name = "food_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String foodName;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보

    @Column
    private String quantity;

    @Column
    private String calories;

    @Column
    private String protein;

    @Column
    private String carbs;

    @Column
    private String fat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private FoodEntity foodEntity;
}
