package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //Optional로 할꺼면 유니크한걸로 아니면 List로
    List<User> findByName(String name);
}
