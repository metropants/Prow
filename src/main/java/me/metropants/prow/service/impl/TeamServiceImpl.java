package me.metropants.prow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropants.prow.entity.entities.Team;
import me.metropants.prow.exception.TeamNotFoundException;
import me.metropants.prow.payload.request.TeamCreateRequest;
import me.metropants.prow.repository.TeamRepository;
import me.metropants.prow.service.TeamService;
import me.metropants.prow.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private static final String NAME_REGEX = "[a-zA-Z0-9\\-]{3,32}$";

    private final TeamRepository repository;
    private final UserService userService;

    @Override
    public Team save(@NotNull String owner, @NotNull TeamCreateRequest request) {
        if (request.name() == null || !request.name().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Team name must be between 3 and 32 characters long and contain only letters and numbers.");
        }

        if (!this.userService.existsByUsername(owner)) {
            throw new IllegalArgumentException("The provided team owner does not exist.");
        }

        Team team = MAPPER.map(request);
        if (team == null) {
            log.error("Failed to map request to team.\nRequest: {}", request);
            return null;
        }

        team.setOwner(owner);
        Team saved = this.repository.save(team);
        log.info("Saving team: {}", saved);
        return saved;
    }

    @Override
    public void deleteById(long id) {
        if (!this.repository.existsById(id)) {
            throw new TeamNotFoundException("The provided team name does not exist.");
        }

        this.repository.deleteById(id);
    }

    @Override
    public void deleteByName(@NotNull String name) {
        if (!this.repository.existsByName(name)) {
            throw new TeamNotFoundException("The provided team name does not exist.");
        }

        this.repository.deleteByName(name);
    }

    @Override
    public void kickMember(long teamId, @NotNull String target) {
        final Team team = this.getById(teamId);
        if (team.getOwner().equals(target)) {
            throw new IllegalArgumentException("You cannot kick yourself from a team.");
        }

        if (!team.isMember(target)) {
            throw new IllegalArgumentException("The provided user is not a member of the team.");
        }

        team.removeMember(target);
        this.repository.save(team);
    }

    @Override
    public boolean isMember(long teamId, @NotNull String member) {
        Team team = this.getById(teamId);
        return team.getOwner().equals(member) || team.getMembers().contains(member);
    }

    @Override
    public Team getById(long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("The provided team id does not exist."));
    }

    @Override
    public Team getByName(@NotNull String name) {
        return this.repository.findByName(name)
                .orElseThrow(() -> new TeamNotFoundException("The provided team name does not exist."));
    }

    @Override
    public List<Team> getTeamsByOwner(@NotNull String owner) {
        return this.repository.findAllByOwner(owner);
    }

    @Override
    public List<Team> getAll() {
        return this.repository.findAll();
    }

}
