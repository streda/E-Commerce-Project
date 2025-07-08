package com.e_commerce.project.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.project.model.persistence.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
