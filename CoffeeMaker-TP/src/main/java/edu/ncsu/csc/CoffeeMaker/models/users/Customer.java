package edu.ncsu.csc.CoffeeMaker.models.users;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 * Customer User
 *
 * @author Emma Holincheck
 * @version 03/31/2023
 *
 */
@Entity
public class Customer extends User {

    /** Customer id */
    @Id
    @GeneratedValue
    private long         id;
    /** List of orders the user has made */
    private List<Order>  orders;
    /** The current orders in the system */
    private OrderService currentOrders;

    /**
     * Creates a new Customer User
     *
     * @param email
     *            the customers email
     * @param name
     *            the name of the customer
     * @param password
     *            the customers password
     */
    public Customer ( final String email, final String name, final String password ) {
        super( email, name, password );
    }

    /**
     * Allows the user to make an order
     *
     * @param order
     *            the order the user is attempting to order
     * @return true if the order can be placed and false if it cannot
     */
    public boolean orderBeverage ( final Order order ) {
        if ( isUsersOrder( order ) ) {
            return false;
        }
        /**
         * This is temporary as it focuses on user implementation depending on
         * how we want to do authentication and order validation we may need to
         * change this method but for the time being this implementation will
         * suffice.
         */
        currentOrders.save( order );
        orders.add( order );
        return true;
    }

    /**
     * Allows the user to check the current status of their order with an
     * orderId
     *
     * @param orderId
     *            the id of the order being checked
     * @return null if the order does not exist or if the order is not one of
     *         the customers orders
     */
    public OrderStatus checkOrderStatus ( final Long orderId ) {
        final Order userOrder = currentOrders.findById( orderId );
        if ( userOrder == null ) {
            return null;
        }
        else if ( !isUsersOrder( userOrder ) ) {
            return null; // We may want this to throw an error in the future
        }
        else {
            return userOrder.getStatus();
        }

    }

    /**
     * Helper method that validates an order is one of the users orders before
     * searching the database for it.
     *
     * @param order
     *            the order being checked
     * @return true if the order is one of the users and false if it is not
     */
    private boolean isUsersOrder ( final Order order ) {
        for ( int i = 0; i < orders.size(); i++ ) {
            if ( orders.get( i ).equals( order ) ) {
                return true;
            }
        }
        return false;
    }

}
