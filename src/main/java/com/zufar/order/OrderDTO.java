package com.zufar.order;

import com.zufar.status.StatusDTO;
import com.zufar.user.UserDTO;
import com.zufar.item.ItemDTO;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private UserDTO user;
    private Set<ItemDTO> Items;
}
