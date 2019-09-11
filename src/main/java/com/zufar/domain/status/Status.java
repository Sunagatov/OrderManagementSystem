package com.zufar.domain.status;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "statuses")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_sequence")
    @SequenceGenerator(name = "status_sequence", sequenceName = "status_seq")
    private Long id;

    @Column(name = "name", length = 256)
    private String name;
}