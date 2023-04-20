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

    /**
     * Finds the customer with the given email
     *
     * @param email
     *            the customer's email
     * @return the customer
     */
    Customer findByEmail ( String email );

}
