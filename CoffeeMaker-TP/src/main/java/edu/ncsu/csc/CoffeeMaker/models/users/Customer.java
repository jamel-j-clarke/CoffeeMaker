package edu.ncsu.csc.CoffeeMaker.models.users;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.management.InvalidAttributeValueException;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * Customer User
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/04/2023
 *
 */
@Entity
public class Customer extends User {

    /** List of orders the user has made */
    @OneToMany
    private List<Order> orders;

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
    }

    /**
     * Creates a default Customer User
     *
     */
    public Customer () {
        super();
    }

    // /**
    // * Allows the user to make an order
    // *
    // * @param order
    // * the order the user is attempting to order
    // * @return true if the order can be placed and false if it cannot
    // */
    // public boolean orderBeverage ( final Order order ) {
    // if ( isUsersOrder( order ) ) {
    // return false;
    // }
    // /**
    // * This is temporary as it focuses on user implementation depending on
    // * how we want to do authentication and order validation we may need to
    // * change this method but for the time being this implementation will
    // * suffice.
    // */
    // orders.add( order );
    // return true;
    // }

    // /**
    // * Getter for the users orders
    // *
    // * @return returns the users orders
    // */
    // public List<Order> getOrders () {
    // return orders;
    // }

    // /**
    // * Allows the user to check the current status of their order with an
    // * orderId
    // *
    // * @param orderId
    // * the id of the order being checked
    // * @return null if the order does not exist or if the order is not one of
    // * the customers orders
    // */
    // public OrderStatus checkOrderStatus ( final Long orderId ) {
    // final Order userOrder = currentOrders.findById( orderId );
    // if ( userOrder == null ) {
    // return null;
    // }
    // else if ( !isUsersOrder( userOrder ) ) {
    // return null; // We may want this to throw an error in the future
    // }
    // else {
    // return userOrder.getStatus();
    // }
    //
    // }

    // /**
    // * Helper method that validates an order is one of the users orders before
    // * searching the database for it.
    // *
    // * @param order
    // * the order being checked
    // * @return true if the order is one of the users and false if it is not
    // */
    // private boolean isUsersOrder ( final Order order ) {
    // return ( order.getUserId() == (Long) super.getId() );
    // }

}
