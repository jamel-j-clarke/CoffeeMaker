package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.management.InvalidAttributeValueException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.users.Employee;
import edu.ncsu.csc.CoffeeMaker.models.users.Manager;
import edu.ncsu.csc.CoffeeMaker.services.EmployeeService;

/**
 * This class tests the Employee Class and its methods
 *
 * @author Erin Grouge
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class EmployeeTest {

    /** The service object used to store the recipes */
    @Autowired
    private EmployeeService service;

    /**
     * Deletes all previously stored recipes before each test
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Test the employee class
     *
     * @throws InvalidAttributeValueException
     *             if the user has invalid input
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    @Test
    @Transactional
    public void testEmployee ()
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        // Employee #1

        final Employee erin = new Employee( "erin@gmail.com", "Erin Grouge", "erin'spassword" );
        // Check that fields were set properly
        assertEquals( "erin@gmail.com", erin.getEmail() );
        assertEquals( "Erin Grouge", erin.getName() );
        assertTrue( erin.checkPassword( "erin'spassword" ) );

        service.save( erin );

        long id = (long) erin.getId();

        // Assert findByEmail and findById work and return the same object
        final Employee serviceEmailErin = service.findByEmail( "erin@gmail.com" );
        final Employee serviceIdErin = service.findById( id );
        assertEquals( serviceEmailErin, serviceIdErin );

        // Assert that persisted copy equals local copy
        assertEquals( serviceEmailErin, erin );
        assertEquals( "erin@gmail.com", serviceEmailErin.getEmail() );
        assertEquals( "Erin Grouge", serviceEmailErin.getName() );
        assertTrue( erin.checkPassword( "erin'spassword" ) );
        assertEquals( id, (long) serviceEmailErin.getId() );

        assertEquals( serviceIdErin, erin );
        assertEquals( "erin@gmail.com", serviceIdErin.getEmail() );
        assertEquals( "Erin Grouge", serviceIdErin.getName() );
        assertTrue( erin.checkPassword( "erin'spassword" ) );
        assertEquals( id, (long) serviceIdErin.getId() );

        // Employee #2

        final Employee emma = new Employee( "emma@gmail.com", "Emma Holincheck", "emma'spassword" );
        // Check that fields were set properly
        assertEquals( "emma@gmail.com", emma.getEmail() );
        assertEquals( "Emma Holincheck", emma.getName() );
        assertTrue( emma.checkPassword( "emma'spassword" ) );

        service.save( emma );

        id = (long) emma.getId();

        // Assert findByEmail and findById work and return the same object
        final Employee serviceEmailEmma = service.findByEmail( "emma@gmail.com" );
        final Employee serviceIdEmma = service.findById( id );
        assertEquals( serviceEmailEmma, serviceIdEmma );

        // Assert that persisted copy equals local copy
        assertEquals( serviceEmailEmma, emma );
        assertEquals( "emma@gmail.com", serviceEmailEmma.getEmail() );
        assertEquals( "Emma Holincheck", serviceEmailEmma.getName() );
        assertTrue( serviceEmailEmma.checkPassword( "emma'spassword" ) );
        assertEquals( id, (long) serviceEmailEmma.getId() );

        assertEquals( serviceIdEmma, emma );
        assertEquals( "emma@gmail.com", serviceIdEmma.getEmail() );
        assertEquals( "Emma Holincheck", serviceIdEmma.getName() );
        assertTrue( serviceIdEmma.checkPassword( "emma'spassword" ) );
        assertEquals( id, (long) serviceIdEmma.getId() );

        // Employee #3

        final Employee jonathan = new Employee( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        // Check that fields were set properly
        assertEquals( "jonathan@gmail.com", jonathan.getEmail() );
        assertEquals( "Jonathan Kurian", jonathan.getName() );
        assertTrue( jonathan.checkPassword( "jonathan'spassword" ) );

        service.save( jonathan );

        id = (long) jonathan.getId();

        // Assert findByEmail and findById work and return the same object
        final Employee serviceEmailJonathan = service.findByEmail( "jonathan@gmail.com" );
        final Employee serviceIdJonathan = service.findById( id );
        assertEquals( serviceEmailJonathan, serviceIdJonathan );

        // Assert that persisted copy equals local copy
        assertEquals( serviceEmailJonathan, jonathan );
        assertEquals( "jonathan@gmail.com", serviceEmailJonathan.getEmail() );
        assertEquals( "Jonathan Kurian", serviceEmailJonathan.getName() );
        assertTrue( jonathan.checkPassword( "jonathan'spassword" ) );
        assertEquals( id, (long) serviceEmailJonathan.getId() );

        assertEquals( serviceIdJonathan, jonathan );
        assertEquals( "jonathan@gmail.com", serviceIdJonathan.getEmail() );
        assertEquals( "Jonathan Kurian", serviceIdJonathan.getName() );
        assertTrue( jonathan.checkPassword( "jonathan'spassword" ) );

        assertEquals( id, (long) serviceIdJonathan.getId() );

        // Check that there are 3 employees
        assertEquals( 3, service.count() );

        // Check equality with same email, name and password
        final Employee jonathanDupe = new Employee( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        service.save( jonathanDupe );
        assertNotEquals( jonathan, jonathanDupe );

        // Check equality with same name and password
        final Employee jonathanDupe2 = new Employee( "jonathan2@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        service.save( jonathanDupe2 );
        assertNotEquals( jonathan, jonathanDupe2 );

    }

    /**
     * Tests updating employee
     *
     * @throws InvalidAttributeValueException
     *             if input is invalid
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    @Test
    @Transactional
    public void testUpdate () throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        final Employee jonathan = new Employee( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        // Check that fields were set properly
        assertEquals( "jonathan@gmail.com", jonathan.getEmail() );
        assertEquals( "Jonathan Kurian", jonathan.getName() );
        assertTrue( jonathan.checkPassword( "jonathan'spassword" ) );

        service.save( jonathan );

        final long id = (long) jonathan.getId();

        // Make duplicate with different email, name, and password
        final Employee jonathanUpdate = new Employee( "jon@gmail.com", "Jon Kurian", "jon'spassword" );
        // Update
        jonathan.updateUser( jonathanUpdate );
        // Check fields updated appropriately - email should not change
        assertEquals( "jonathan@gmail.com", jonathan.getEmail() );
        assertEquals( "Jon Kurian", jonathan.getName() );
        assertTrue( jonathanUpdate.checkPassword( "jon'spassword" ) );
        assertEquals( id, jonathan.getId() ); // id should not change

        // Save
        service.save( jonathan );

        // Retrieve Jonathan from service
        final Employee serviceJonUpdated = service.findById( id );
        // Check fields updated appropriately - email should not change
        assertEquals( "jonathan@gmail.com", serviceJonUpdated.getEmail() );
        assertEquals( "Jon Kurian", serviceJonUpdated.getName() );
        assertTrue( serviceJonUpdated.checkPassword( "jon'spassword" ) );
        assertEquals( id, serviceJonUpdated.getId() ); // id should not change

        // Check that only 1 Jonathan exists
        assertEquals( 1, service.count() );
    }

    /**
     * Tests error checking for invalid input
     *
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    @Test
    @Transactional
    public void testInvalidInput () throws InvalidKeySpecException, NoSuchAlgorithmException {

        // Empty email
        try {
            final Employee erin = new Employee( "", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Empty name
        try {
            final Employee erin = new Employee( "erin@gmail.com", "", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Empty password
        try {
            final Employee erin = new Employee( "erin@gmail.com", "Erin Grouge", "" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Null email
        try {
            final Employee erin = new Employee( null, "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Null name
        try {
            final Employee erin = new Employee( "erin@gmail.com", null, "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Null password
        try {
            final Employee erin = new Employee( "erin@gmail.com", "Erin Grouge", null );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }

        // Check email formatting
        try {
            final Employee erin = new Employee( "erin@gmail", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( "erin@gmail.", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( "erin@.com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( "erin@.", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( "erin.com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( "erin.gmail@com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( "erin.@com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( "erin.@", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( ".@", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Employee erin = new Employee( "@gmail.com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }

    }

    /**
     * Tests the Manager class
     *
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     * @throws InvalidAttributeValueException
     *             if there is an error
     *
     */
    @Test
    @Transactional
    public void testManager ()
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        final Manager man = new Manager();
        assertTrue( Manager.checkEmail( "m4n4g3r@csc326.edu" ) );
        assertTrue( man.checkPassword( "tuffyhunttalleyhill" ) );

        assertFalse( Manager.checkEmail( "manager@ncsu.edu" ) );
        assertFalse( man.checkPassword( "tuffyhunttallyhill" ) );

        assertFalse( Manager.checkEmail( "" ) );
        assertFalse( man.checkPassword( "" ) );

        assertFalse( Manager.checkEmail( null ) );
        assertFalse( man.checkPassword( null ) );

    }

    /**
     * Checks the user's password against the one passed
     *
     * @throws InvalidAttributeValueException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     * @throws NoSuchAlgorithmException
     *             if there is an error
     */
    @Test
    @Transactional
    public void testCheckPassword ()
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        final Employee employee = new Employee( "customer@gmail.com", "Customer", "password" );
        assertTrue( employee.checkPassword( "password" ) );
        assertFalse( employee.checkPassword( "passwOrd" ) );
    }
}
