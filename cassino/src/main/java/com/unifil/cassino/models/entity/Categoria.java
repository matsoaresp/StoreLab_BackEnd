package com.unifil.cassino.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Categoria {

    public Categoria(String descricao,
                     String nome,
                     Long id,
                     List<Projeto> projetos) {
        this.descricao = descricao;
        this.nome = nome;
        this.id = id;
        this.projetos = projetos;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    @OneToMany(mappedBy = "categoria")
    private List<Projeto> projetos;
}