package com.unifil.appstore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestProjetoDto {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 150, message = "Título deve ter no máximo 150 caracteres")
    private String titulo;

    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String descricao;

    @Size(max = 255, message = "Link do repositório deve ter no máximo 255 caracteres")
    private String linkRepositorio;

    @Size(max = 4, message = "Um projeto pode ter no máximo 5 integrantes (incluindo o criador)")
    private List<Long> membrosIds;
}