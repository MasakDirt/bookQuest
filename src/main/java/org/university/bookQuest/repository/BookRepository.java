package org.university.bookQuest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.university.bookQuest.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByAuthorId(long authorId, Pageable pageable);
    List<Book> findAllByAuthorId(long authorId);
}
