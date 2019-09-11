package com.zufar.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    
    private Long id;
    private ProductDTO product;
    private Long quantity;
}
