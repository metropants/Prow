package me.metropants.prow.repository;

import me.metropants.prow.entity.entities.TeamInvite;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamInviteRepository extends JpaRepository<TeamInvite, Long> {

    Optional<TeamInvite> findByTeamIdAndInvitee(long teamId, @NotNull String invitee);

    Optional<TeamInvite> findByInvitee(@NotNull String invitee);

    List<TeamInvite> findAllByInvitee(@NotNull String invitee);

}
