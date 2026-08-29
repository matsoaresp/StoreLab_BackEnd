package com.unifil.appstore.service;

import com.unifil.appstore.dto.request.RequestProjetoDto;
import com.unifil.appstore.dto.response.ResponseProjetoDto;
import com.unifil.appstore.dto.response.ResponseUsuarioResumoDto;
import com.unifil.appstore.enums.person.PersonRole;
import com.unifil.appstore.models.Projeto;
import com.unifil.appstore.models.Usuario;
import com.unifil.appstore.repository.ProjetoRepository;
import com.unifil.appstore.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjetoService {

    private static final int LIMITE_TOTAL_INTEGRANTES = 5;

    private final ProjetoRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ProjetoService(ProjetoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ResponseProjetoDto criarProjeto(RequestProjetoDto dto, Long criadorId, PersonRole role) {
        if (role == PersonRole.STUDENT) {
            boolean jaTemProjeto = repository.existsByCriador_IdAndExcluidoEmIsNull(criadorId);
            if (jaTemProjeto) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Você já possui um projeto criado. Cada aluno pode criar apenas um projeto.");
            }
        }

        Optional<Usuario> criadorOptional = usuarioRepository.findById(criadorId);
        if (criadorOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário criador não encontrado");
        }
        Usuario criador = criadorOptional.get();

        Set<Usuario> membros = resolverMembros(dto.getMembrosIds(), criadorId);

        Projeto projeto = new Projeto();
        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());
        projeto.setLinkRepositorio(dto.getLinkRepositorio());
        projeto.setCriadoEm(Instant.now());
        projeto.setCriador(criador);
        projeto.setMembros(membros);

        repository.save(projeto);

        return converterParaDto(projeto);
    }

    public ResponseProjetoDto encontrarProjeto(Long id) {
        Projeto projeto = buscarProjetoAtivo(id);
        return converterParaDto(projeto);
    }

    public List<ResponseProjetoDto> listarProjetos() {
        List<Projeto> projetos = repository.findAllByExcluidoEmIsNull();

        List<ResponseProjetoDto> resultado = new ArrayList<>();
        for (Projeto projeto : projetos) {
            resultado.add(converterParaDto(projeto));
        }
        return resultado;
    }

    public List<ResponseProjetoDto> listarMeusProjetos(Long usuarioId) {
        List<Projeto> projetosCriados = repository.findAllByCriador_IdAndExcluidoEmIsNull(usuarioId);
        List<Projeto> projetosParticipando = repository.findAllByMembros_IdAndExcluidoEmIsNull(usuarioId);

        Set<Projeto> projetosSemRepetir = new HashSet<>();
        projetosSemRepetir.addAll(projetosCriados);
        projetosSemRepetir.addAll(projetosParticipando);

        List<ResponseProjetoDto> resultado = new ArrayList<>();
        for (Projeto projeto : projetosSemRepetir) {
            resultado.add(converterParaDto(projeto));
        }
        return resultado;
    }

    @Transactional
    public ResponseProjetoDto atualizarProjeto(Long id, RequestProjetoDto dto, PersonRole role) {
        validarPodeGerenciar(role);
        Projeto projeto = buscarProjetoAtivo(id);

        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());
        projeto.setLinkRepositorio(dto.getLinkRepositorio());
        projeto.setAtualizadoEm(Instant.now());

        if (dto.getMembrosIds() != null) {
            Set<Usuario> novosMembros = resolverMembros(dto.getMembrosIds(), projeto.getCriador().getId());
            projeto.setMembros(novosMembros);
        }

        repository.save(projeto);
        return converterParaDto(projeto);
    }

    @Transactional
    public void inativarProjeto(Long id, PersonRole role) {
        validarPodeGerenciar(role);
        Projeto projeto = buscarProjetoAtivo(id);

        projeto.setInativadoEm(Instant.now());
        repository.save(projeto);
    }

    @Transactional
    public void reativarProjeto(Long id, PersonRole role) {
        validarPodeGerenciar(role);
        Projeto projeto = buscarProjetoAtivo(id);

        projeto.setInativadoEm(null);
        repository.save(projeto);
    }

    @Transactional
    public void excluirProjeto(Long id, PersonRole role) {
        validarPodeGerenciar(role);
        Projeto projeto = buscarProjetoAtivo(id);

        projeto.setExcluidoEm(Instant.now());
        repository.save(projeto);
    }

    private Projeto buscarProjetoAtivo(Long id) {
        Optional<Projeto> projetoOptional = repository.findById(id);

        if (projetoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado");
        }

        Projeto projeto = projetoOptional.get();

        if (projeto.getExcluidoEm() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado");
        }

        return projeto;
    }

    private void validarPodeGerenciar(PersonRole role) {
        if (role != PersonRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas professores podem gerenciar projetos");
        }
    }

    private Set<Usuario> resolverMembros(List<Long> membrosIds, Long criadorId) {
        if (membrosIds == null || membrosIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<Long> idsUnicos = new HashSet<>(membrosIds);

        if (idsUnicos.contains(criadorId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O criador não deve ser informado na lista de membros");
        }

        if (idsUnicos.size() + 1 > LIMITE_TOTAL_INTEGRANTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Um projeto pode ter no máximo " + LIMITE_TOTAL_INTEGRANTES + " integrantes, incluindo o criador");
        }

        List<Usuario> usuariosEncontrados = usuarioRepository.findAllById(idsUnicos);

        if (usuariosEncontrados.size() != idsUnicos.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais membros informados não existem");
        }

        return new HashSet<>(usuariosEncontrados);
    }

    private ResponseProjetoDto converterParaDto(Projeto projeto) {
        ResponseProjetoDto dto = new ResponseProjetoDto();
        dto.setId(projeto.getId());
        dto.setTitulo(projeto.getTitulo());
        dto.setDescricao(projeto.getDescricao());
        dto.setLinkRepositorio(projeto.getLinkRepositorio());
        dto.setCriadoEm(projeto.getCriadoEm());
        dto.setDataPublicacao(projeto.getDataPublicacao());
        dto.setAtualizadoEm(projeto.getAtualizadoEm());
        dto.setAtivo(projeto.getInativadoEm() == null);
        dto.setCriador(converterUsuarioResumo(projeto.getCriador()));

        List<ResponseUsuarioResumoDto> membrosDto = new ArrayList<>();
        for (Usuario membro : projeto.getMembros()) {
            membrosDto.add(converterUsuarioResumo(membro));
        }
        dto.setMembros(membrosDto);

        return dto;
    }

    private ResponseUsuarioResumoDto converterUsuarioResumo(Usuario usuario) {
        return new ResponseUsuarioResumoDto(usuario.getId(), usuario.getNome());
    }
}