package com.unifil.appstore.models.entity;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

    @OneToMany(mappedBy = "categoria")
    private List<Projeto> projetos;
}