package com.unifil.cassino.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private Double saldo;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private boolean ativo;

    public Usuario() {
        this.dataCriacao = LocalDateTime.now();
        this.saldo = 0.0;
        this.ativo = true;
    }

    public Usuario(Long id, String nome, String email, String senha,
                   Double saldo, LocalDateTime dataCriacao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.saldo = saldo;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", saldo=" + saldo +
                ", dataCriacao=" + dataCriacao +
                ", ativo=" + ativo +
                '}';
    }
}