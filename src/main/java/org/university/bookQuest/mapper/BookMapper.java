package org.university.bookQuest.mapper;

import org.mapstruct.Mapper;
import org.university.bookQuest.dto.BookRequest;
import org.university.bookQuest.entity.Book;

@Mapper
public interface BookMapper {

    Book getBookFromBookRequest(BookRequest bookRequest);
}
