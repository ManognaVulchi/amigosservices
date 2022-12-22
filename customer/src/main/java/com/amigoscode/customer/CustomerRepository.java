package com.amigoscode.customer;

import org.springframework.data.jpa.repository.JpaRepository;
//entity is customer datatype for id is int
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
