package me.metropants.prow.controller.controllers.team;

import lombok.RequiredArgsConstructor;
import me.metropants.prow.entity.entities.Task;
import me.metropants.prow.payload.request.TaskCreateRequest;
import me.metropants.prow.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams/{teamID}/tasks")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TeamTaskController {

    private final TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable long teamID) {
        final List<Task> tasks = this.taskService.findAllByTeamId(teamID);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/")
    public ResponseEntity<Task> createTask(@PathVariable long teamID, @RequestBody TaskCreateRequest request) {
        final Task createdTask = this.taskService.save(teamID, request);
        if (createdTask == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(createdTask);
    }

    @PatchMapping("/{taskID}")
    public ResponseEntity<Task> updateTask(@PathVariable long teamID, @PathVariable long taskID, @RequestBody TaskCreateRequest request) {
        final Task updatedTask = this.taskService.update(teamID, taskID, request);
        if (updatedTask == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskID}")
    public ResponseEntity<?> deleteTask(@PathVariable long teamID, @PathVariable long taskID) {
        this.taskService.delete(teamID, taskID);
        return ResponseEntity.ok().build();
    }

}
