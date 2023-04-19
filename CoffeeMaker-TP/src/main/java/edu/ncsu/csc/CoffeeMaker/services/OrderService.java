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
 * @version 04/17/2023
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

}
