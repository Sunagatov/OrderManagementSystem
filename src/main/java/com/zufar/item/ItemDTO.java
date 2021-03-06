package com.zufar.item;

import com.zufar.product.ProductDTO;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
