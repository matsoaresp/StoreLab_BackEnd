package com.unifil.appstore.dto.response;

import com.unifil.appstore.enums.person.PersonRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUsuarioDto {

    private Long id;
    private String nome;
    private String email;
    private String login;
    private PersonRole role;
    private boolean ativo;
    private LocalDateTime dataCriacao;

}