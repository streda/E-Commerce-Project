package com.e_commerce.project.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.project.model.persistence.Cart;
import com.e_commerce.project.model.persistence.User;

import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
