package me.metropants.prow.controller.controllers.team;

import lombok.RequiredArgsConstructor;
import me.metropants.prow.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams/{teamID}/members")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamService teamService;

    @PostMapping("/{username}")
    public ResponseEntity<?> kickMember(@PathVariable long teamID, @PathVariable String username) {
        this.teamService.kickMember(teamID, username);
        return ResponseEntity.ok().build();
    }

}
