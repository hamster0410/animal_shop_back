package animal_shop.shop.pet.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AnimalWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String species;

    private Long low_weight;

    private Long high_weight;

    @Column(length = 30)
    private String size;
}
