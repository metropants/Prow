package me.metropants.prow.entity.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.metropants.prow.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "team_invites")
@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeamInvite extends BaseEntity {

    @JsonProperty("team_id")
    private long teamId;

    private String inviter;

    private String invitee;

    private LocalDateTime expires = LocalDateTime.now().plusMinutes(1);

}
