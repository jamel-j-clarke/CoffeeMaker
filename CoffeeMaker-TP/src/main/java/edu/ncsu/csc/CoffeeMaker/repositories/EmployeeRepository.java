package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.users.Employee;

/**
 * Employee Repository
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 *
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // /**
    // * Finds an Employee object with the provided id. Spring will generate
    // code
    // * to make this happen.
    // *
    // * @param id
    // * of the employee
    // *
    // * @return Found employee, null if none.
    // */
    // Employee findById ( long id );

    /**
     * Finds the customer with the given email
     *
     * @param email
     *            the customer's email
     * @return the customer
     */
    Employee findByEmail ( String email );

}
