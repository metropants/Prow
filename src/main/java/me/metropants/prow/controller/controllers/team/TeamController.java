package me.metropants.prow.controller.controllers.team;

import lombok.RequiredArgsConstructor;
import me.metropants.prow.entity.entities.Team;
import me.metropants.prow.payload.request.TeamCreateRequest;
import me.metropants.prow.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/")
    public ResponseEntity<List<Team>> getAllSelfTeams(@AuthenticationPrincipal String username) {
        List<Team> teams = this.teamService.getTeamsByOwner(username);
        return ResponseEntity.ok(teams);
    }

    @PostMapping("/")
    public ResponseEntity<Team> createTeam(@AuthenticationPrincipal String username, @RequestBody TeamCreateRequest request) {
        Team team = this.teamService.save(username, request);
        if (team == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(team);
    }

}
