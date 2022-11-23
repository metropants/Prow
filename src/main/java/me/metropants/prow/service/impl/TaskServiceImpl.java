package me.metropants.prow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropants.prow.entity.entities.Task;
import me.metropants.prow.entity.entities.Team;
import me.metropants.prow.exception.TaskNotFoundException;
import me.metropants.prow.payload.request.TaskCreateRequest;
import me.metropants.prow.repository.TaskRepository;
import me.metropants.prow.service.TaskService;
import me.metropants.prow.service.TeamService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final TeamService teamService;

    private void checkAccess(long teamID) {
        final String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!this.teamService.isMember(teamID, user)) {
            throw new IllegalArgumentException("You are not a member of this team.");
        }
    }

    @Override
    public Task save(long teamID, @NotNull TaskCreateRequest request) {
        this.checkAccess(teamID);

        final Team team = this.teamService.getById(teamID);
        final Task task = MAPPER.map(request);
        if (task == null) {
            log.error("Failed to map request to task.\nRequest: {}", request);
            return null;
        }

        task.setTeam(team);
        return this.repository.save(task);
    }

    @Override
    public Task update(long teamID, long taskID, @NotNull TaskCreateRequest request) {
        this.checkAccess(teamID);

        return this.repository.findById(taskID)
                .map(task -> {
                    final Task updated = MAPPER.map(request);
                    if (updated == null) {
                        log.error("Failed to map request to task.\nRequest: {}", request);
                        return null;
                    }

                    updated.setId(taskID);
                    return this.repository.save(task);
                })
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskID + " not found."));
    }

    @Override
    public void delete(long teamID, long id) {
        this.checkAccess(teamID);
        this.repository.deleteById(id);
    }

    @Override
    public Task findById(long teamID, long id) {
        this.checkAccess(teamID);
        return this.repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found."));
    }

    @Override
    public List<Task> findAllByTeamId(long teamId) {
        this.checkAccess(teamId);
        return this.repository.findAllByTeamId(teamId);
    }

}
