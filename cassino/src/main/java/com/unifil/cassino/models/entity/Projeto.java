package com.unifil.cassino.models.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "projetos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Projeto {
    public Projeto(
            Usuario usuario,
            LocalDateTime dataCriacao,
            String status,
            String titulo,
            Long id,
            String descricao,
            String objetivo,
            String imagemCapa,
            Categoria categoria) {
        this.usuario = usuario;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.titulo = titulo;
        this.id = id;
        this.descricao = descricao;
        this.objetivo = objetivo;
        this.imagemCapa = imagemCapa;
        this.categoria = categoria;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String objetivo;

    private String imagemCapa;

    @Column(nullable = false)
    private String status;

    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


}