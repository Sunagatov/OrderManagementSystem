package com.zufar.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderDTO {
    
    private Long id;
    private String title;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private StatusDTO status;
    private CustomerDTO customer;
    private Set<ItemDTO> orderItems;
}
