package com.example.spring_discordexample.join.repository;

import com.example.spring_discordexample.join.entity.JoinInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinRepository extends JpaRepository<JoinInfo, Long> {

    Optional<JoinInfo> findByDiscordId(String discordId);

    Boolean existsByDiscordId(String discordId);

    @Transactional
    void deleteByDiscordId(String discordId);
}
