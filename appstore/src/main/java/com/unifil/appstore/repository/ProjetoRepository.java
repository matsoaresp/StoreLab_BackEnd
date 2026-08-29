package com.unifil.appstore.repository;

import com.unifil.appstore.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;






public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findAllByExcluidoEmIsNull();

    List<Projeto> findAllByCriador_IdAndExcluidoEmIsNull(Long criadorId);

    List<Projeto> findAllByMembros_IdAndExcluidoEmIsNull(Long usuarioId);

    boolean existsByCriador_IdAndExcluidoEmIsNull(Long criadorId);
}