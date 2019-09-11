package com.zufar.domain.status;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "statuses")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_sequence")
    @SequenceGenerator(name = "status_sequence", sequenceName = "status_seq")
    private Long id;

    @Column(name = "name", length = 256)
    private String name;
}
