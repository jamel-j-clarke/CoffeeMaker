package edu.ncsu.csc.CoffeeMaker.models.users;

import javax.persistence.Entity;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 *
 * @author Emma Holincheck
 * @version 03/31/2023
 *
 */
@Entity
public class Employee extends User {
    /**
     * Creates a new Employee user
     *
     * @param email
     *            of the user
     * @param name
     *            of the user
     * @param password
     *            of the user
     */
    public Employee ( final String email, final String name, final String password ) {
        super( email, name, password );
    }

    /**
     * Allows the employee to fulfill an order passed to the system
     *
     * @param order
     */
    public static void fulfillOrder ( final Order order ) {
        Order.complete();
    }

}
