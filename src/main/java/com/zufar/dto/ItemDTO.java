package com.zufar.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemDTO {
    
    private Long id;
    private ProductDTO product;
    private Long quantity;
}
