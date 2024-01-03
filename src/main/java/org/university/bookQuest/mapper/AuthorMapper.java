package org.university.bookQuest.mapper;

import org.mapstruct.Mapper;
import org.university.bookQuest.dto.AuthorRequest;
import org.university.bookQuest.entity.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author getAuthorFromAuthorRequest(AuthorRequest authorRequest);
}
