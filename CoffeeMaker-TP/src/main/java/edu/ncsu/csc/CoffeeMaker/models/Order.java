package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.List;

public class Order extends DomainObject {

    /** The id of the order */
    private long         id;
    /** The payment for the order */
    private long         payment;
    /** THe list of beverages in the order */
    private List<Recipe> beverages;

    /**
     * Constructs a new Order
     *
     * @param beverages
     */
    public Order ( final List<Recipe> beverages ) {

    }

    @Override
    public Serializable getId () {
        // TODO Auto-generated method stub
        return null;
    }

}
