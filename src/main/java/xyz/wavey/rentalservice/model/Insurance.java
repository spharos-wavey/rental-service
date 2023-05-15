package xyz.wavey.rentalservice.model;

import jakarta.persistence.*;
import lombok.*;
import xyz.wavey.rentalservice.base.BaseTimeEntity;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Insurance extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "varchar(50)")
    private String content;
}
