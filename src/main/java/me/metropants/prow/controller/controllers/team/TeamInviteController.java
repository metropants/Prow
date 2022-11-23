package me.metropants.prow.controller.controllers.team;

import lombok.RequiredArgsConstructor;
import me.metropants.prow.entity.entities.TeamInvite;
import me.metropants.prow.payload.request.TeamInviteRequest;
import me.metropants.prow.service.TeamInviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams/invites")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TeamInviteController {

    private final TeamInviteService inviteService;

    @GetMapping("/")
    public ResponseEntity<List<TeamInvite>> getAllInvites(@AuthenticationPrincipal String username) {
        List<TeamInvite> invites = this.inviteService.getInvites(username);
        return ResponseEntity.ok(invites);
    }

    @PostMapping("/")
    public ResponseEntity<?> createInvite(@AuthenticationPrincipal String username, @RequestBody TeamInviteRequest request) {
        this.inviteService.createInvite(username, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{teamId}")
    public ResponseEntity<?> acceptInvite(@AuthenticationPrincipal String username, @PathVariable long teamId) {
        this.inviteService.acceptInvite(teamId, username);
        return ResponseEntity.ok().build();
    }

}
