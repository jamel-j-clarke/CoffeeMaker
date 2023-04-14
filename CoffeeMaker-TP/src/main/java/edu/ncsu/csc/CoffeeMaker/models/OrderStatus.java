package edu.ncsu.csc.CoffeeMaker.models;

/**
 * Creates the Enums for the order status in the system
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 */
public enum OrderStatus {
    /** When an order has not been started */
    NOT_STARTED,
    /** When an order is in the progress of being made */
    IN_PROGRESS,
    /** When an order is complete */
    DONE,
    /** When an order has been cancelled */
    CANCELLED,
    /** When an order has been picked up by the customer */
    PICKED_UP
}
