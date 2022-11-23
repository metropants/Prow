package me.metropants.prow.entity.entities;

import lombok.*;
import me.metropants.prow.entity.BaseEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "teams")
@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Team extends BaseEntity {

    @Column(nullable = false, length = 32)
    private String name;

    @Column(nullable = false, length = 32)
    private String owner;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "team_members")
    private Set<String> members = new HashSet<>();

    @OneToMany(mappedBy = "team")
    private Set<Task> todos = new HashSet<>();

    public void addMember(@NotNull String member) {
        this.members.add(member);
    }

    public void removeMember(@NotNull String member) {
        this.members.remove(member);
    }

    public boolean isMember(@NotNull String member) {
        return this.members.contains(member);
    }

}
