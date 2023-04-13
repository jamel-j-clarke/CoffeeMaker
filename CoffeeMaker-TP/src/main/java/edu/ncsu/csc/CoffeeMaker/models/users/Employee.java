package edu.ncsu.csc.CoffeeMaker.models.users;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.management.InvalidAttributeValueException;
import javax.persistence.Entity;

/**
 * This class represents an Employee of the Beverage Making company. An employee
 * can fulfill beverage orders.
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/10/2023
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
     * @throws InvalidAttributeValueException
     *             if the employees email is invalid
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    public Employee ( final String email, final String name, final String password )
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        super( email, name, password );
    }

    /**
     * Creates a default Employee user
     *
     */
    public Employee () {
        super();
    }

    // /**
    // * Adding this from the DomainObject
    // */
    // @Override
    // public Serializable getId () {
    // return this.id;
    // }

    // /**
    // * Allows the employee to fulfill an order passed to the system
    // *
    // * @param order
    // * the order that should be fulfilled
    // */
    // public static void fulfillOrder ( final Order order ) {
    // final Order newOrder = orders.findById( order.getLongId() );
    // newOrder.complete();
    // orders.save( newOrder );
    // }

    // /**
    // * Allows the employee to cancel an order passed to the system
    // *
    // * @param order
    // * the order that should be cancelled
    // */
    // public static void cancelOrder ( final Order order ) {
    // final Order newOrder = orders.findById( order.getLongId() );
    // newOrder.cancel();
    // orders.save( newOrder );
    // }

    // /**
    // * Allows the employee to update the inventory
    // *
    // * @param inventory
    // * the updated inventory.
    // */
    // public void updateInventory ( final Inventory inventory ) {
    // final Inventory ivt = currentInventory.getInventory();
    // ivt.addIngredients( inventory.getIngredients() );
    // currentInventory.save( ivt );
    // }

    // /**
    // * Returns a list of completed orders for the employee
    // *
    // * @return list of completed orders
    // */
    // public List<Order> getCompletedOrdersList () {
    // final JpaRepository<Order, Long> completedOrder =
    // orders.getCompletedOrders();
    // final List<Order> completedOrders = completedOrder.findAll();
    // return completedOrders;
    // }

}
