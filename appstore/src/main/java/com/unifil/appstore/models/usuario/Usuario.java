package com.unifil.appstore.models.usuario;
import com.unifil.appstore.enums.person.PersonRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String nome;
    private String email;
    private String matricula;
    private String senha;
    private LocalDateTime dataCriacao;
    private PersonRole role;
    private boolean ativo;

    public Usuario(String nome, String email, String matricula, String senha) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.senha = senha;
    }
}