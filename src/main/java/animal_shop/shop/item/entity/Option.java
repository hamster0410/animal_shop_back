package animal_shop.shop.item.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Setter
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;


    @Column(nullable = false)
    private String name;

    private Long price;

    // 생성자, Getter, Setter
}