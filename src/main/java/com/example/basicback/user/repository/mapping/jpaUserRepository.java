package com.example.basicback.user.repository.mapping;

import com.example.basicback.user.entity.pk.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface jpaUserRepository extends JpaRepository<User, String> {
}