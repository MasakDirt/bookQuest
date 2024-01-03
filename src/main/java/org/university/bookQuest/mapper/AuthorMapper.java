package org.university.bookQuest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.university.bookQuest.dto.AuthorRequest;
import org.university.bookQuest.entity.Author;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", imports = {LocalDate.class, DateTimeFormatter.class})
public interface AuthorMapper {

    @Mapping(target = "birthDate", expression = "java(LocalDate.parse(authorRequest.getBirthDate().trim(), DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")).atStartOfDay())")
    Author getAuthorFromAuthorRequest(AuthorRequest authorRequest);

    @Mapping(target = "birthDate", expression = "java(author.getBirthDate().format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))")
    AuthorRequest getAuthorRequestFromAuthor(Author author);
}
