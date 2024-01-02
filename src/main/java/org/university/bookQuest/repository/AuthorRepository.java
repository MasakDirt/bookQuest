package org.university.bookQuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.university.bookQuest.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
