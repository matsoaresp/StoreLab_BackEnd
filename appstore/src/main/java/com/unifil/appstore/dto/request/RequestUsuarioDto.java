package com.unifil.appstore.dto.request;
import com.unifil.appstore.enums.person.PersonRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUsuarioDto {

    Long id;
    private String nome;
    private String email;
    private String senha;
    private PersonRole role;
}
