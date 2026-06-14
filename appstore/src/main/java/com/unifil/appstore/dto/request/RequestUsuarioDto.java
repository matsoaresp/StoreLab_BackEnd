package com.unifil.appstore.dto.request;
import com.unifil.appstore.enums.person.PersonRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUsuarioDto {

    private Long id;
    private String nome;
    private String email;
    private PersonRole role;
}
