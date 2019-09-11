package com.zufar.domain.order;


import com.zufar.domain.item.Item;
import com.zufar.domain.status.Status;
import com.zufar.domain.customer.Customer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter @Setter @EqualsAndHashCode @ToString
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_seq")
    private Long id;

    @Column(name = "title", length = 256)
    private String title;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "status_id")
    private Status status;

    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "order_order_items",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "order_item_id")})
    private Set<Item> items;
}
