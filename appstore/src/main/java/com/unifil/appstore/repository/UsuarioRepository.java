package com.unifil.appstore.repository;

import com.unifil.appstore.enums.person.PersonRole;
import com.unifil.appstore.models.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    List<Usuario> findAllByRole(PersonRole role);
    Optional<Usuario> findByIdAndRole(Long id, PersonRole role);
}