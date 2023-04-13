package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;
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
    private OrderRepository    orderRepository;

    /**
     * OrderRepository that is only meant to be a temporary repository for the
     * employee user to access not started orders and not to save the orders in
     * a separate location. No tags necessary
     */
    private OrderRepository    incompleteOrders;

    /**
     * OrderRepository that is only meant to be a temporary repository for the
     * employee user to access inprogress orders and not to save the orders in a
     * separate location. No tags necessary
     */
    private OrderRepository    inprogressOrders;

    /**
     * OrderRepository that is only meant to be a temporary repository for the
     * employee user to access completed orders and not to save the orders in a
     * separate location. No tags necessary
     */
    private OrderRepository    completedOrders;

    /**
     * OrderRepository that is only meant to be a temporary repository for the
     * employee user to access picked up orders and not to save the orders in a
     * separate location. No tags necessary
     */
    private OrderRepository    pickedUpOrders;

    /**
     * CustomerRepository that is only meant to be a temporary repository for
     * the employee user to access picked up orders and not to save the orders
     * in a separate location. No tags necessary
     */
    private CustomerRepository customers;

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

    /**
     * Returns a repository of not started orders
     *
     * @return a repository of not started orders
     */
    public JpaRepository<Order, Long> getincompleteOrders () {
        // Ensures we are working with an empty temp repository for the time
        // being
        incompleteOrders.deleteAll();
        final List<Order> tempOrders = orderRepository.findAll();
        for ( int i = 0; i < tempOrders.size(); i++ ) {
            if ( tempOrders.get( i ).getStatus().equals( OrderStatus.NOT_STARTED ) ) {
                incompleteOrders.save( tempOrders.get( i ) );
            }
        }
        return incompleteOrders;
    }

    /**
     * Returns a repository of inprogress orders
     *
     * @return a repository of inprogress orders
     */
    public JpaRepository<Order, Long> getIncompleteOrders () {
        // Ensures we are working with an empty temp repository for the time
        // being
        inprogressOrders.deleteAll();
        final List<Order> tempOrders = orderRepository.findAll();
        for ( int i = 0; i < tempOrders.size(); i++ ) {
            if ( tempOrders.get( i ).getStatus().equals( OrderStatus.IN_PROGRESS ) ) {
                inprogressOrders.save( tempOrders.get( i ) );
            }
        }
        return inprogressOrders;
    }

    /**
     * Returns a repository of picked up orders
     *
     * @return a repository of picked up orders
     */
    public JpaRepository<Order, Long> getPickedupOrders () {
        // Ensures we are working with an empty temp repository for the time
        // being
        pickedUpOrders.deleteAll();
        final List<Order> tempOrders = orderRepository.findAll();
        for ( int i = 0; i < tempOrders.size(); i++ ) {
            if ( tempOrders.get( i ).getStatus().equals( OrderStatus.PICKED_UP ) ) {
                pickedUpOrders.save( tempOrders.get( i ) );
            }
        }
        return pickedUpOrders;
    }

    /**
     * Returns all records of this type that exist in the database. If you want
     * more precise ways of retrieving an individual record (or collection of
     * records) see `findBy(Example)`
     *
     * @return All records stored in the database.
     */
    public List<Order> findCustomerOrders ( final String email ) {
        final List<Order> tempOrders = orderRepository.findAll();
        final List<Order> customerOrders = orderRepository.findAll();
        for ( int i = 0; i < tempOrders.size(); i++ ) {
            if ( tempOrders.get( i ).getUserEmail() == email ) {
                customerOrders.add( tempOrders.get( i ) );
            }
        }
        return customerOrders;
    }

    /**
     * Returns all records of this type that exist in the database. If you want
     * more precise ways of retrieving an individual record (or collection of
     * records) see `findBy(Example)`
     *
     * @return All records stored in the database.
     */
    public List<Order> findIncompleteOrders () {
        return incompleteOrders.findAll();
    }

    /**
     * Returns all records of this type that exist in the database. If you want
     * more precise ways of retrieving an individual record (or collection of
     * records) see `findBy(Example)`
     *
     * @return All records stored in the database.
     */
    public List<Order> findInprogressOrders () {
        return inprogressOrders.findAll();
    }

    /**
     * Returns all records of this type that exist in the database. If you want
     * more precise ways of retrieving an individual record (or collection of
     * records) see `findBy(Example)`
     *
     * @return All records stored in the database.
     */
    public List<Order> findCompletedOrders () {
        return completedOrders.findAll();
    }

    /**
     * Returns all records of this type that exist in the database. If you want
     * more precise ways of retrieving an individual record (or collection of
     * records) see `findBy(Example)`
     *
     * @return All records stored in the database.
     */
    public List<Order> findPickedUpOrders () {
        return pickedUpOrders.findAll();
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
