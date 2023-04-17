package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;

/**
 * Employee Repository
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/17/2023
 *
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds order objects with the provided status. Spring will generate code
     * to make this happen.
     *
     * @param status
     *            status of the order
     * @return Found order(s), null if none.
     */
    List<Order> findByStatus ( OrderStatus status );
}
