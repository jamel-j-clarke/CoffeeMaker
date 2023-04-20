package edu.ncsu.csc.CoffeeMaker.models.users;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;
import javax.persistence.Entity;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * Customer User
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/13/2023
 *
 */
@Entity
public class Customer extends User {

    /** List of orders the user has made */
    // @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // private final List<Order> orders;
    private final ArrayList<Long> orders;

    /**
     * Creates a new Customer User
     *
     * @param email
     *            the customers email
     * @param name
     *            the name of the customer
     * @param password
     *            the customers password
     * @throws InvalidAttributeValueException
     *             if the customers email is invalid
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    public Customer ( final String email, final String name, final String password )
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        super( email, name, password );
        // orders = new ArrayList<Order>();
        orders = new ArrayList<Long>();
    }

    /**
     * Creates a default Customer User
     *
     */
    public Customer () {
        super();
        // orders = new ArrayList<Order>();
        orders = new ArrayList<Long>();
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
            orders.add( (Long) order.getId() );
            return true;
        }
        return false;
    }

    /**
     * Allows the user to make an order
     *
     * @param order
     *            the order the user is attempting to order
     * @return true if the order can be placed and false if it cannot
     */
    public boolean cancelOrder ( final Order order ) {
        return orders.remove( order.getId() );
    }

    /**
     * Getter for the users orders
     *
     * @return returns the users orders
     */
    public List<Long> getOrders () {
        return orders;
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
        return ( order.getUserEmail().equals( this.getEmail() ) );
    }

}
