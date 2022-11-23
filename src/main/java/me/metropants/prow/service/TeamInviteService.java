package me.metropants.prow.service;

import me.metropants.prow.entity.entities.TeamInvite;
import me.metropants.prow.payload.request.TeamInviteRequest;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface TeamInviteService {

    @Mapper(componentModel = "spring")
    interface TeamInviteMapper {

        TeamInviteMapper INSTANCE = Mappers.getMapper(TeamInviteMapper.class);

        @Mapping(target = "id", ignore = true)
        TeamInvite map(TeamInviteRequest request);

    }

    TeamInviteMapper MAPPER = TeamInviteMapper.INSTANCE;

    void createInvite(@NotNull String inviter, @NotNull TeamInviteRequest request);

    void acceptInvite(long teamId, @NotNull String invitee);

    List<TeamInvite> getInvites(@NotNull String invitee);

}
