package me.metropants.prow.service;

import me.metropants.prow.entity.entities.Task;
import me.metropants.prow.payload.request.TaskCreateRequest;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface TaskService {

    @Mapper(componentModel = "spring")
    interface TaskMapper {

        TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

        @Mapping(target = "id", ignore = true)
        Task map(TaskCreateRequest request);

    }

    TaskMapper MAPPER = TaskMapper.INSTANCE;

    Task save(long teamID, @NotNull TaskCreateRequest request);

    Task update(long teamID, long taskID, @NotNull TaskCreateRequest request);

    void delete(long teamID, long id);

    Task findById(long teamID, long id);

    List<Task> findAllByTeamId(long teamId);

}
