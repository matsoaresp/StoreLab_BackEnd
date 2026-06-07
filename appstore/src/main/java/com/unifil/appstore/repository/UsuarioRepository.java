package com.unifil.appstore.repository;
import com.unifil.appstore.models.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
}
