package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

import edu.ncsu.csc.CoffeeMaker.models.users.Customer;

/**
 * The order object that tracks the recipes in the order for the user
 *
 * @author Emma Holincheck
 * @version 04/04/2023
 *
 */
@Entity
public class Order extends DomainObject {

    /** The id of the order */
    @Id
    @GeneratedValue
    private long               id;
    /** The payment for the order */
    @Min ( 0 )
    private final long         payment;
    /** The list of beverages in the order */
    @OneToMany
    private final List<Recipe> beverages;
    /** Representative of the current status of the order in the system */
    private static OrderStatus orderStatus;
    /** The id of the user placing the order */
    private final long         userId;

    /**
     * Constructs a new Order
     *
     * @param beverages
     *            the list of beverages in the order
     * @param payment
     *            the payment method for the order
     */
    public Order ( final List<Recipe> beverages, final long payment, final Customer customer ) {
        this.beverages = beverages;
        this.payment = payment;
        userId = customer.getIdLong();
        orderStatus = OrderStatus.NOT_STARTED;
    }

    /**
     * Gets the payment for the order
     *
     * @return payment information for the order
     */
    public long getPayment () {
        return payment;
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
     * Gets the id of the user who placed the order
     *
     * @return userId of the user who placed the order
     */
    public long getUserId () {
        return userId;
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

    /**
     * Equals method for Order object
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Order other = (Order) obj;
        if ( beverages == null ) {
            if ( other.beverages != null ) {
                return false;
            }
        }
        else if ( !beverages.equals( other.beverages ) ) {
            return false;
        }
        if ( id != other.id ) {
            return false;
        }
        if ( payment != other.payment ) {
            return false;
        }
        return true;
    }

}
