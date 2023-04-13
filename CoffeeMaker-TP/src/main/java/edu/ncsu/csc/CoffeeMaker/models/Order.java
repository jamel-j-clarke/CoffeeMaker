package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

import edu.ncsu.csc.CoffeeMaker.models.users.Customer;

/**
 * The order object that tracks the recipes in the order for the user
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/04/2023
 *
 */
@Entity
public class Order extends DomainObject {

    /** The id of the order */
    @Id
    @GeneratedValue
    private Long               id;
    /** The payment for the order */
    @Min ( 0 )
    private final long         payment;
    /** The beverage in the order */
    @OneToMany
    private final Recipe       beverage;
    /** Representative of the current status of the order in the system */
    @Enumerated ( EnumType.STRING )
    private static OrderStatus orderStatus;
    /** The id of the user placing the order */
    private final String       userEmail;

    /**
     * Constructs a new Order
     *
     * @param beverages
     *            the list of beverages in the order
     * @param payment
     *            the payment method for the order
     */
    public Order ( final Recipe beverage, final long payment, final Customer customer ) {
        this.beverage = beverage;
        this.payment = payment;
        userEmail = customer.getEmail();
        orderStatus = OrderStatus.NOT_STARTED;
    }

    /**
     * Constructs a default order
     */
    public Order () {
        userEmail = "";
        payment = 0;
        beverage = null;
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
     * Gets the beverage for the order
     *
     * @return beverage information for the order
     */
    public Recipe getRecipe () {
        return beverage;
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
     * Gets the email of the user who placed the order
     *
     * @return userEmail of the user who placed the order
     */
    public String getUserEmail () {
        return userEmail;
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
     * Cancels the order, moving its status to cancelled in the system
     */
    public void pickup () {
        orderStatus = OrderStatus.PICKEDUP;
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
        if ( beverage == null ) {
            if ( other.beverage != null ) {
                return false;
            }
        }
        else if ( !beverage.equals( other.beverage ) ) {
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
