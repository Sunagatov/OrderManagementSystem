package com.zufar.domain.customer;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "сustomers")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    @SequenceGenerator(name = "customer_sequence", sequenceName = "customer_seq")
    private Long id;

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "email", length = 256)
    private String email;

    @Column(name = "login", length = 256)
    private String login;

    @Column(name = "password", length = 256)
    private String password;
}
