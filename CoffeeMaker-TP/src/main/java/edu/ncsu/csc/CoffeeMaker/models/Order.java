package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * The order object that tracks the recipes in the order for the user
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 *
 */
@Entity
public class Order extends DomainObject {

    /** The id of the order */
    @Id
    @GeneratedValue
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
     * Returns the long version of the id of the order
     *
     * @return long version of the id
     */
    public long getLongId () {
        return id;
    }

    /**
     * Starts the order and moves its status to in progress in the system
     */
    public void start () {
        orderStatus = OrderStatus.IN_PROGRESS;
    }

    /**
     * Completes the order, moving its status to done in the system
     */
    public void complete () {
        orderStatus = OrderStatus.DONE;
    }

    /**
     * Cancels the order, moving its status to cancelled in the system
     */
    public void cancel () {
        orderStatus = OrderStatus.CANCELLED;
    }

    /**
     * Returns the current status of the given order
     *
     * @return the current order status
     */
    public OrderStatus getStatus () {
        return orderStatus;
    }

}
