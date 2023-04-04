package edu.ncsu.csc.CoffeeMaker.models.users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 *
 * @author Emma Holincheck
 * @version 03/31/2023
 *
 */
@Entity
public class Employee extends User {
    /** The id of the employee */
    @Id
    @GeneratedValue
    private long                id;
    /** The current orders in the system */
    private static OrderService orders;
    /** The current system inventory */
    static InventoryService     currentInventory;

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
     *            the order that should be fulfilled
     */
    public static void fulfillOrder ( final Order order ) {
        final Order newOrder = orders.findById( order.getLongId() );
        newOrder.complete();
        orders.save( newOrder );
    }

    /**
     * Allows the employee to cancel an order passed to the system
     *
     * @param order
     *            the order that should be cancelled
     */
    public static void cancelOrder ( final Order order ) {
        final Order newOrder = orders.findById( order.getLongId() );
        newOrder.cancel();
        orders.save( newOrder );
    }

    /**
     * Allows the employee to update the inventory
     *
     * @param inventory
     *            the updated inventory.
     */
    public void updateInventory ( final Inventory inventory ) {
        final Inventory ivt = currentInventory.getInventory();
        ivt.addIngredients( inventory.getIngredients() );
        currentInventory.save( ivt );
    }

}
