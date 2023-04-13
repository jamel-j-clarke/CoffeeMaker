package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.users.Customer;

/**
 * Customer Repository
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // /**
    // * Finds a Customer object with the provided id. Spring will generate code
    // * to make this happen.
    // *
    // * @param id
    // * of the employee
    // *
    // * @return Found employee, null if none.
    // */
    // Customer findById ( long id );

    /**
     * Finds the customer with the given email
     *
     * @param email
     *            the customer's email
     * @return the customer
     */
    Customer findByEmail ( String email );

}
