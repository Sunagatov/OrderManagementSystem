package com.zufar.domain.product;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_seq")
    private Long id;

    @Column(name = "name", length = 256)
    private String name;
}
