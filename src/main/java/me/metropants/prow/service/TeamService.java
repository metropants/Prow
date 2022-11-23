package me.metropants.prow.service;

import me.metropants.prow.entity.entities.Team;
import me.metropants.prow.payload.request.TeamCreateRequest;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface TeamService {

    @Mapper(componentModel = "spring")
    interface TeamMapper {

        TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

        @Mapping(target = "id", ignore = true)
        Team map(TeamCreateRequest request);

    }

    TeamMapper MAPPER = TeamMapper.INSTANCE;

    Team save(@NotNull String owner, @NotNull TeamCreateRequest request);

    void deleteById(long id);

    void deleteByName(@NotNull String name);

    void kickMember(long teamId, @NotNull String member);

    boolean isMember(long teamId, @NotNull String member);

    Team getById(long id);

    Team getByName(@NotNull String name);

    List<Team> getTeamsByOwner(@NotNull String owner);

    List<Team> getAll();

}
