package com.unifil.appstore.repository;

import com.unifil.appstore.enums.person.PersonRole;
import com.unifil.appstore.models.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CredencialRepository extends JpaRepository<Credencial, Long> {
    Optional<Credencial> findByLogin(String login);
    boolean existsByLogin(String login);
    Optional<Credencial> findByUsuario_IdAndRole(Long usuarioId, PersonRole role);
    List<Credencial> findAllByRole(PersonRole role);
}