package me.metropants.prow.service;

import me.metropants.prow.entity.entities.User;
import me.metropants.prow.payload.request.RegisterRequest;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    @Mapper(componentModel = "spring")
    interface UserMapper {

        UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

        @Mapping(target = "id", ignore = true)
        User map(RegisterRequest request);

    }

    UserMapper MAPPER = UserMapper.INSTANCE;

    /**
     * @param request the request to register a new user containing the username and password.
     * @return the newly created {@link User} instance.
     */
    User save(@NotNull RegisterRequest request);

    void deleteById(long id);

    void deleteByUsername(@NotNull String username);

    User getById(long id);

    User getByUsername(@NotNull String username);

    List<User> getAll();

}
