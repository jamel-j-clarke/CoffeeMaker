package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.users.Employee;
import edu.ncsu.csc.CoffeeMaker.repositories.EmployeeRepository;

/**
 * Employee Service
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 *
 */
@Component
@Transactional
public class EmployeeService extends Service<Employee, Long> {

    /**
     * EmployeeRepository, to be autowired in by Spring and provide CRUD
     * operations on Employee model.
     */
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Gets the current employee repository
     *
     * @return the current employee repository
     */
    @Override
    protected JpaRepository<Employee, Long> getRepository () {
        return employeeRepository;
    }

    /**
     * Find an employee with the provided name
     *
     * @param id
     *            id of the employee to find
     * @return found employee, null if none
     */
    public Employee findById ( final long id ) {
        return employeeRepository.findById( id );
    }
}
