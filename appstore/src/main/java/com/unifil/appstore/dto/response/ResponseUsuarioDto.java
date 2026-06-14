package com.unifil.appstore.dto.response;


import com.unifil.appstore.enums.person.PersonRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUsuarioDto {


    Long id;
    private String nome;
    private String email;
    private PersonRole role;



}
