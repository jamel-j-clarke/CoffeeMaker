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

    /**
     * Finds the customer with the given email
     *
     * @param email
     *            the customer's email
     * @return the customer
     */
    Employee findByEmail ( String email );

}
