package me.metropants.prow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropants.prow.entity.entities.Team;
import me.metropants.prow.entity.entities.TeamInvite;
import me.metropants.prow.payload.request.TeamInviteRequest;
import me.metropants.prow.repository.TeamInviteRepository;
import me.metropants.prow.service.TeamInviteService;
import me.metropants.prow.service.TeamService;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamInviteServiceImpl implements TeamInviteService {

    private final Queue<TeamInvite> invites = new ConcurrentLinkedDeque<>();
    private final TeamInviteRepository repository;
    private final TeamService teamService;

    @Scheduled(initialDelay = 60, fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    private void expireInvites() {
        if (this.invites.isEmpty()) {
            return;
        }

        while (!this.invites.isEmpty() && invites.peek() != null) {
            if (!invites.peek().getExpires().isBefore(LocalDateTime.now())) {
                continue;
            }

            TeamInvite invite = this.invites.poll();
            if (invite == null) {
                continue;
            }

            this.repository.delete(invite);
            log.info("Deleted expired invite for user {}.", invite.getInvitee());
            break;
        }
    }

    @Override
    public void createInvite(@NotNull String inviter, @NotNull TeamInviteRequest request) {
        Team team = this.teamService.getByName(request.team());
        if (team == null) {
            log.warn("Team {} does not exist", request.team());
            return;
        }

        if (team.isMember(request.invitee())) {
            throw new IllegalArgumentException("User is already a member of this team.");
        }

        TeamInvite invite = MAPPER.map(request);
        if (invite == null) {
            log.error("Failed to map request to team invite.\nRequest: {}", request);
            throw new IllegalArgumentException("Failed to create team invite.");
        }

        invite.setTeamId(team.getId());
        invite.setInviter(inviter);
        this.repository.save(invite);
        this.invites.add(invite);
    }

    @Override
    public void acceptInvite(long teamId, @NotNull String invitee) {
        this.repository.findByTeamIdAndInvitee(teamId, invitee)
                .ifPresentOrElse(invite -> {
                    final Team team = this.teamService.getById(teamId);

                    team.addMember(invitee);
                    this.repository.delete(invite);
                    this.invites.remove(invite);
                    log.info("Accepted invite for team {} by {}.", teamId, invitee);
                }, () -> {
                    throw new IllegalArgumentException("No invite or invite has expired for the provided team.");
                });
    }

    @Override
    public List<TeamInvite> getInvites(@NotNull String invitee) {
        return this.repository.findAllByInvitee(invitee);
    }

}
