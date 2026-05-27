package com.unifil.appstore.models.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String matricula;
    private String email;
    private String senha;
    private LocalDateTime dataCriacao;
    private boolean ativo;


    public Usuario() {
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }
}