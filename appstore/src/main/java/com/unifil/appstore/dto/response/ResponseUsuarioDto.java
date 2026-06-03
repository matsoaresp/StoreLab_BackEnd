package com.unifil.appstore.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUsuarioDto {


    Long id;
    private String email;
    private String matricula;

    public ResponseUsuarioDto(Long id, String matricula, String email) {
        this.id = id;
        this.matricula = matricula;
        this.email = email;
    }


}
