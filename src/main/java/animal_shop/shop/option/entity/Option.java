package animal_shop.shop.option.entity;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@ToString
@Builder
@Entity
@Getter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long price;


}
