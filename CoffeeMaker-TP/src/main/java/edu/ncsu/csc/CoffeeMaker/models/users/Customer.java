package edu.ncsu.csc.CoffeeMaker.models.users;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.Order;

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
    private long        id;

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
     */
    public Customer ( final String email, final String name, final String password ) {
        super( email, name, password );
    }

}
