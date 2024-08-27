package com.example.basicback.repository.mapping;

import com.example.basicback.model.pk.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface jpaUserRepository extends JpaRepository<User, String> {

}
