package me.metropants.prow.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import me.metropants.prow.entity.entities.Task;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskCreateRequest(String title, String description, Task.Status status) {}
