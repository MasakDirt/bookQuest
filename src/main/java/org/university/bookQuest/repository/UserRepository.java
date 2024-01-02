package org.university.bookQuest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.university.bookQuest.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
