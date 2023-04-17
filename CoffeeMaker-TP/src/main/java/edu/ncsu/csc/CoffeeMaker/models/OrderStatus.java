package edu.ncsu.csc.CoffeeMaker.models;

/**
 * Creates the Enums for the order status in the system
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/17/2023
 */
public enum OrderStatus {
    /** When an order has not been started */
    NOT_STARTED,
    /** When an order is in the progress of being made */
    IN_PROGRESS,
    /** When an order is complete */
    DONE,
    /** When an order has been cancelled */
    CANCELLED
}
