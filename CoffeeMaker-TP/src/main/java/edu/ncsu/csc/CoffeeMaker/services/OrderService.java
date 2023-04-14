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
 * @version 04/04/2023
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
     * OrderRepository that is only meant to be a temporary repository for the
     * employee user to access completed orders and not to save the orders in a
     * separate location. No tags necessary
     */
    private OrderRepository completedOrders;

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
     * Returns a repository of only completed orders
     *
     * @return a repository of only completed orders
     */
    public JpaRepository<Order, Long> getCompletedOrders () {
        // Ensures we are working with an empty temp repository for the time
        // being
        completedOrders.deleteAll();
        final List<Order> tempOrders = orderRepository.findAll();
        for ( int i = 0; i < tempOrders.size(); i++ ) {
            if ( tempOrders.get( i ).getStatus().equals( OrderStatus.DONE ) ) {
                completedOrders.save( tempOrders.get( i ) );
            }
        }
        return completedOrders;
    }

}
