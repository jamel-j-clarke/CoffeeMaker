package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.users.Customer;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;

/**
 * Customer Service
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 *
 */
@Component
@Transactional
public class CustomerService extends Service<Customer, Long> {

    /**
     * CustomerRepository, to be autowired in by Spring and provide CRUD
     * operations on Customer model.
     */
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Gets the current customer repository
     *
     * @return the current customer repository
     */
    @Override
    protected JpaRepository<Customer, Long> getRepository () {
        return customerRepository;
    }

    // /**
    // * Find a customer with the provided id
    // *
    // * @param id
    // * id of the customer
    // * @return found customer, null if none
    // */
    // public Customer findById ( final long id ) {
    // return customerRepository.findById( id );
    // }

    /**
     * Find a customer with the provided id
     *
     * @param email
     *            email of the customer
     * @return found customer, null if none
     */
    public Customer findByEmail ( final String email ) {
        return customerRepository.findByEmail( email );
    }
}
