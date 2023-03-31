package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

/**
 * Order Service
 *
 * @author Emma Holincheck
 * @version 03/30/2023
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
     * Find an order with the provided id
     *
     * @param id
     *            id of the order to find
     * @return found order, null if none
     */
    public Order findById ( final long id ) {
        return orderRepository.findById( id );
    }

}
