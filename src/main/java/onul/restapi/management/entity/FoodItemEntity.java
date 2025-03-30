package onul.restapi.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onul.restapi.member.entity.Members;

import java.util.Map;

@Entity
@Table(name = "food_items",
        indexes = {
                @Index(name = "idx_member_id", columnList = "member_id"),
                @Index(name = "idx_recipe_id", columnList = "recipe_id")
        })
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
