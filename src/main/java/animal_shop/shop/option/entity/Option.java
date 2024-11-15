package animal_shop.shop.option.entity;

import animal_shop.shop.item.entity.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;


}
