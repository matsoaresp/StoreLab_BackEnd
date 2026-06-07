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
public class ResponseAuthenticationDto {

    private String token;
    private String tipo;
    private Long userId;
    private String nome;
    private String email;
    private PersonRole role;
    private Long tempoExpiracao;
}

