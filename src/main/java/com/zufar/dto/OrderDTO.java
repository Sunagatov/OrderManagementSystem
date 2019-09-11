package com.zufar.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    
    private Long id;
    private String title;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private StatusDTO status;
    private CustomerDTO customer;
    private Set<ItemDTO> Items;
}
