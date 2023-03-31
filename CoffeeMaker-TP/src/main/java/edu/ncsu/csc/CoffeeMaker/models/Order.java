package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.List;

/**
 * The order object that tracks the recipes in the order for the user
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 *
 */
public class Order extends DomainObject {

    /** The id of the order */
    private long               id;
    /** The payment for the order */
    private long               payment;
    /** The list of beverages in the order */
    private final List<Recipe> beverages;
    /** Representative of the current status of the order in the system */
    private static OrderStatus orderStatus;

    /**
     * Constructs a new Order
     *
     * @param beverages
     */
    public Order ( final List<Recipe> beverages ) {
        this.beverages = beverages;
        orderStatus = OrderStatus.NOT_STARTED;
    }

    /**
     * Gets the id of the order
     *
     * @return id of the order
     */
    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Starts the order and moves its status to in progress in the system
     */
    public static void start () {
        orderStatus = OrderStatus.IN_PROGRESS;
    }

    /**
     * Completes the order, moving its status to done in the system
     */
    public static void complete () {
        orderStatus = OrderStatus.DONE;
    }

    /**
     * Cancels the order, moving its status to cancelled in the system
     */
    public static void cancel () {
        orderStatus = OrderStatus.CANCELLED;
    }

    /**
     * Returns the current status of the given order
     *
     * @return the current order status
     */
    public static OrderStatus getStatus () {
        return orderStatus;
    }

}
