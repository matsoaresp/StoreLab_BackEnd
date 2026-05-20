package com.unifil.cassino.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comentarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    private LocalDateTime dataComentario;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    public Comentarios(Long id,
                       Projeto projeto,
                       Usuario usuario,
                       LocalDateTime dataComentario,
                       String texto) {
        this.id = id;
        this.projeto = projeto;
        this.usuario = usuario;
        this.dataComentario = dataComentario;
        this.texto = texto;
    }
}