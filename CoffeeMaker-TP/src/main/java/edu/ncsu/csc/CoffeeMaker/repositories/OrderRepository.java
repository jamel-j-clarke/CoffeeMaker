package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * Employee Repository
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 *
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Finds an Order object with the provided id. Spring will generate code to
     * make this happen.
     *
     * @param id
     *            of the order
     *
     * @return Found order, null if none.
     */
    Order findById ( long id );

}
