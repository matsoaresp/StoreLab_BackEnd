package com.unifil.appstore.repository;

import com.unifil.appstore.models.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokensRepository extends JpaRepository<Tokens, Long> {

    Optional<Tokens> findByToken(String token);
}
