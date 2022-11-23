package me.metropants.prow.repository;

import me.metropants.prow.entity.entities.Team;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    void deleteByName(@NotNull String name);

    boolean existsByName(@NotNull String name);

    Optional<Team> findByName(@NotNull String name);

    List<Team> findAllByOwner(@NotNull String owner);

}
