package com.zufar.user;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.GenerationType;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_seq")
    private Long id;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "email", length = 256, nullable = false)
    private String email;

    @Column(name = "login", length = 256, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 256, nullable = false)
    private String password;
}


