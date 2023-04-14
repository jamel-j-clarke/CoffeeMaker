package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * Employee Repository
 *
 * @author Emma Holincheck
 * @version 04/04/2023
 *
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

}
