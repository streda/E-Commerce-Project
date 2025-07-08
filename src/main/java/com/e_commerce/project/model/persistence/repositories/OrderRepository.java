package com.e_commerce.project.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.project.model.persistence.User;
import com.e_commerce.project.model.persistence.UserOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
