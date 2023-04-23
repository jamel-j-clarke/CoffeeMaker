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

    /**
     * Finds order objects with the provided status. Spring will generate code
     * to make this happen.
     *
     * @param userEmail
     *            email of the customer
     * @return Found order(s), null if none.
     */
    List<Order> findByUserEmail ( String userEmail );

    /**
     * Finds orders by their status and the users email
     *
     * @param status
     *            the status of the order this will change depending on whether
     *            this function is being used to generate the order history
     *            list, the in-progress list, the barista order options list and
     *            so on
     * @param userEmail
     *            the email of the user that placed the order
     * @return a list of orders that meet the parameter criteria
     */
    List<Order> findByStatusNotAndUserEmail ( final OrderStatus status, final String userEmail );
}
