package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

/**
 * Order Service
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/20/2023
 *
 */
@Component
@Transactional
public class OrderService extends Service<Order, Long> {
    /**
     * OrderRepository, to be autowired in by Spring and provide CRUD operations
     * on Order model.
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Gets the current order repository
     *
     * @return the current order repository
     */
    @Override
    protected JpaRepository<Order, Long> getRepository () {
        return orderRepository;
    }

    /**
     * Find orders with the provided status
     *
     * @param status
     *            status of the orders to find
     * @return found orders, null if none
     */
    public List<Order> findByStatus ( final OrderStatus status ) {
        return orderRepository.findByStatus( status );
    }

    /**
     * Find orders with the provided status
     *
     * @param userEmail
     *            email of the customer's orders to find
     * @return found orders, null if none
     */
    public List<Order> findByUserEmail ( final String userEmail ) {
        return orderRepository.findByUserEmail( userEmail );
    }

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
    public List<Order> findByStatusNotAndUserEmail ( final OrderStatus status, final String userEmail ) {
        return orderRepository.findByStatusNotAndUserEmail( status, userEmail );
    }

}
