package me.metropants.prow.entity.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.metropants.prow.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "tasks")
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {

    public enum Status {

        TODO, IN_PROGRESS, DONE

    }

    @JsonIgnore
    @ManyToOne
    private Team team;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Status status = Status.TODO;

}
