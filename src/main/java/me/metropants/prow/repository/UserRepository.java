package me.metropants.prow.repository;

import me.metropants.prow.entity.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    void deleteByUsername(@NotNull String username);

    boolean existsByUsername(@NotNull String username);

    Optional<User> findUserByUsername(@NotNull String username);

}
