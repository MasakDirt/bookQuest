package org.university.bookQuest.mapper;

import org.mapstruct.Mapper;
import org.university.bookQuest.dto.RegisterRequest;
import org.university.bookQuest.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User getUserFromRegisterRequest(RegisterRequest registerRequest);
}
