package com.unifil.appstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProjetoDto {

    private Long id;
    private String titulo;
    private String descricao;
    private String linkRepositorio;
    private Instant criadoEm;
    private Instant dataPublicacao;
    private Instant atualizadoEm;
    private boolean ativo;
    private ResponseUsuarioResumoDto criador;
    private List<ResponseUsuarioResumoDto> membros;
}