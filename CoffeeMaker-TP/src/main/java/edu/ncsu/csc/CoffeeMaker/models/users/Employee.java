package edu.ncsu.csc.CoffeeMaker.models.users;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.management.InvalidAttributeValueException;
import javax.persistence.Entity;

/**
 * This class represents an Employee of the Beverage Making company. An employee
 * can fulfill beverage orders.
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/10/2023
 *
 */
@Entity
public class Employee extends User {

    /**
     * Creates a new Employee user
     *
     * @param email
     *            of the user
     * @param name
     *            of the user
     * @param password
     *            of the user
     * @throws InvalidAttributeValueException
     *             if the employees email is invalid
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    public Employee ( final String email, final String name, final String password )
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        super( email, name, password );
    }

    /**
     * Creates a default Employee user
     *
     */
    public Employee () {
        super();
    }

}
