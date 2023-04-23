package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

/**
 * The order object that tracks the recipes in the order for the user
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/20/2023
 *
 */
@Entity
@Table ( name = "orders" )
public class Order extends DomainObject {

    /** The id of the order */
    @Id
    @GeneratedValue
    private Long          id;
    /** The payment for the order */
    @Min ( 0 )
    private final Integer payment;
    /** The list of beverages in the order */
    private final String  beverage;
    /** Representative of the current status of the order in the system */
    @Enumerated ( EnumType.STRING )
    private OrderStatus   status;
    /** The id of the user placing the order */
    private final String  userEmail;

    /**
     * Constructs a new Order
     *
     * @param beverage
     *            the beverages in the order
     * @param payment
     *            the payment method for the order
     * @param email
     *            the email of the customer that ordered the beverage.
     */
    public Order ( final String beverage, final Integer payment, final String email ) {
        this.beverage = beverage;
        this.payment = payment;
        userEmail = email;
        status = OrderStatus.NOT_STARTED;
    }

    /**
     * Constructs a default order
     */
    public Order () {
        userEmail = "";
        payment = 0;
        beverage = "";
    }

    /**
     * Gets the payment for the order
     *
     * @return payment information for the order
     */
    public Integer getPayment () {
        return payment;
    }

    /**
     * Gets the beverage for the order
     *
     * @return beverage information for the order
     */
    public String getRecipe () {
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
     * Returns the beverage ordered
     *
     * @return beverage
     */
    public String getBeverage () {
        return this.beverage;
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
     *
     * @return true if the order can be started, false if not.
     */
    public boolean start () {
        if ( status == OrderStatus.NOT_STARTED ) {
            status = OrderStatus.IN_PROGRESS;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Completes the order, moving its status to done in the system.
     *
     * @return true if the order can be completed, false if not.
     */
    public boolean complete () {
        if ( status == OrderStatus.IN_PROGRESS ) {
            status = OrderStatus.DONE;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Cancels the order, moving its status to cancelled in the system
     */
    public void cancel () {
        status = OrderStatus.CANCELLED;
    }

    /**
     * Cancels the order, moving its status to cancelled in the system
     *
     * @return true if the order can be picked up, false if not.
     */
    public boolean pickup () {
        if ( status == OrderStatus.DONE ) {
            status = OrderStatus.PICKED_UP;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the current status of the given order
     *
     * @return the current order status
     */
    public OrderStatus getStatus () {
        return status;
    }

    /**
     * Equals method for Order object
     *
     * @param obj
     *            the object being compared to the current order
     * @return true if the two objects are both equal orders and false if they
     *         are not
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
        // Checks the case if beverage is null
        if ( beverage == null ) {
            if ( other.beverage != null ) {
                return false;
            }
        }
        else if ( !beverage.equals( other.beverage ) ) {
            return false;
        }
        // Checks the case if the user email is null. This check is crucial
        // because if we have a guest user their email may appear null in the
        // system and so we need to account for that flow
        if ( userEmail == null ) {
            if ( other.userEmail != null ) {
                return false;
            }
        }
        else if ( !userEmail.equals( other.userEmail ) ) {
            return false;
        }
        if ( (long) id != (long) other.id ) {
            return false;
        }
        if ( (int) payment != (int) other.payment ) {
            return false;
        }
        return true;
    }

    /**
     * Calculates the object's hashcode.
     */
    @Override
    public int hashCode () {
        return Objects.hash( beverage, id, payment, status, userEmail );
    }

    /**
     * To String method for the Order object that constructs a string with the
     * order id, beverage, payment, user email, and status.
     *
     * @return string representation of the order
     */
    @Override
    public String toString () {
        return "Order " + id + " of " + beverage + " for $" + payment + " placed by " + userEmail + " has status "
                + status;
    }

}
