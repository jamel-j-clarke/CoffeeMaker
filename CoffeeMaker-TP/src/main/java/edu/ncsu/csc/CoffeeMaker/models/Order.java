package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.List;

public class Order extends DomainObject {

    /** The id of the order */
    private long               id;
    /** The payment for the order */
    private long               payment;
    /** THe list of beverages in the order */
    private final List<Recipe> beverages;

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

    @Override
    public Serializable getId () {
        // TODO Auto-generated method stub
        return null;
    }

    public static void start () {
        orderStatus = OrderStatus.IN_PROGRESS;
    }

    public static void complete () {
        orderStatus = OrderStatus.DONE;
    }

    public static void cancel () {
        orderStatus = OrderStatus.CANCELLED;
    }

    public static OrderStatus getStatus () {
        return orderStatus;
    }

}
