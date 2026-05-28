package com.unifil.appstore.models.usuario;
import com.unifil.appstore.enums.person.PersonRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String matricula;
    private String email;
    private String senha;
    private LocalDateTime dataCriacao;
    private PersonRole role;
    private boolean ativo;


    public Usuario() {
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }
}