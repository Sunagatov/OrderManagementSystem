package com.zufar.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CustomerDTO {
    
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
}
