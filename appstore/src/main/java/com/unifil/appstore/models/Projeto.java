package com.unifil.appstore.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_projeto")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column()
    private String descricao;

    private String linkRepositorio;

    @Column(updatable = false)
    private Instant criadoEm;

    private Instant dataPublicacao;
    private Instant atualizadoEm;
    private Instant excluidoEm;
    private Instant inativadoEm;

    @ManyToOne()
    @JoinColumn(name = "criador_id", nullable = false)
    private Usuario criador;

    @ManyToMany()
    @JoinTable(
            name = "tb_projeto_membros",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> membros = new HashSet<>();
}