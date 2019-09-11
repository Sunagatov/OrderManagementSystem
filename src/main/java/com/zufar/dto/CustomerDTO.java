package com.zufar.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
}
