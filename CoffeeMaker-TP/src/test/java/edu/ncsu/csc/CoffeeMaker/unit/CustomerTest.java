package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javax.management.InvalidAttributeValueException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.users.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;

/**
 * Tests the Customer class and its methods
 *
 * @author Erin Grouge
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CustomerTest {

    /** The service object used to store the recipes */
    @Autowired
    private CustomerService service;

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
     */
    @Test
    @Transactional
    public void testCustomer () throws InvalidAttributeValueException {
        // Employee #1

        final Customer erin = new Customer( "erin@gmail.com", "Erin Grouge", "erin'spassword" );
        // Check that fields were set properly
        assertEquals( "erin@gmail.com", erin.getEmail() );
        assertEquals( "Erin Grouge", erin.getName() );
        assertEquals( "erin'spassword", erin.getPassword() );

        service.save( erin );

        long id = (long) erin.getId();

        // Assert findByEmail and findById work and return the same object
        final Customer serviceEmailErin = service.findByEmail( "erin@gmail.com" );
        final Customer serviceIdErin = service.findById( id );
        assertEquals( serviceEmailErin, serviceIdErin );

        // Assert that persisted copy equals local copy
        assertEquals( serviceEmailErin, erin );
        assertEquals( "erin@gmail.com", serviceEmailErin.getEmail() );
        assertEquals( "Erin Grouge", serviceEmailErin.getName() );
        assertEquals( "erin'spassword", serviceEmailErin.getPassword() );
        assertEquals( id, (long) serviceEmailErin.getId() );

        assertEquals( serviceIdErin, erin );
        assertEquals( "erin@gmail.com", serviceIdErin.getEmail() );
        assertEquals( "Erin Grouge", serviceIdErin.getName() );
        assertEquals( "erin'spassword", serviceIdErin.getPassword() );
        assertEquals( id, (long) serviceIdErin.getId() );

        // Employee #2

        final Customer emma = new Customer( "emma@gmail.com", "Emma Holincheck", "emma'spassword" );
        // Check that fields were set properly
        Assertions.assertEquals( "emma@gmail.com", emma.getEmail() );
        Assertions.assertEquals( "Emma Holincheck", emma.getName() );
        Assertions.assertEquals( "emma'spassword", emma.getPassword() );

        service.save( emma );

        id = (long) emma.getId();

        // Assert findByEmail and findById work and return the same object
        final Customer serviceEmailEmma = service.findByEmail( "emma@gmail.com" );
        final Customer serviceIdEmma = service.findById( id );
        assertEquals( serviceEmailEmma, serviceIdEmma );

        // Assert that persisted copy equals local copy
        assertEquals( serviceEmailEmma, emma );
        assertEquals( "emma@gmail.com", serviceEmailEmma.getEmail() );
        assertEquals( "Emma Holincheck", serviceEmailEmma.getName() );
        assertEquals( "emma'spassword", serviceEmailEmma.getPassword() );
        assertEquals( id, (long) serviceEmailEmma.getId() );

        assertEquals( serviceIdEmma, emma );
        assertEquals( "emma@gmail.com", serviceIdEmma.getEmail() );
        assertEquals( "Emma Holincheck", serviceIdEmma.getName() );
        assertEquals( "emma'spassword", serviceIdEmma.getPassword() );
        assertEquals( id, (long) serviceIdEmma.getId() );

        // Employee #3

        final Customer jonathan = new Customer( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        // Check that fields were set properly
        assertEquals( "jonathan@gmail.com", jonathan.getEmail() );
        assertEquals( "Jonathan Kurian", jonathan.getName() );
        assertEquals( "jonathan'spassword", jonathan.getPassword() );

        service.save( jonathan );

        id = (long) jonathan.getId();

        // Assert findByEmail and findById work and return the same object
        final Customer serviceEmailJonathan = service.findByEmail( "jonathan@gmail.com" );
        final Customer serviceIdJonathan = service.findById( id );
        assertEquals( serviceEmailJonathan, serviceIdJonathan );

        // Assert that persisted copy equals local copy
        assertEquals( serviceEmailJonathan, jonathan );
        assertEquals( "jonathan@gmail.com", serviceEmailJonathan.getEmail() );
        assertEquals( "Jonathan Kurian", serviceEmailJonathan.getName() );
        assertEquals( "jonathan'spassword", serviceEmailJonathan.getPassword() );
        assertEquals( id, (long) serviceEmailJonathan.getId() );

        assertEquals( serviceIdJonathan, jonathan );
        assertEquals( "jonathan@gmail.com", serviceIdJonathan.getEmail() );
        assertEquals( "Jonathan Kurian", serviceIdJonathan.getName() );
        assertEquals( "jonathan'spassword", serviceIdJonathan.getPassword() );
        assertEquals( id, (long) serviceIdJonathan.getId() );

        // Check that there are 3 employees
        assertEquals( 3, service.count() );

        // Check equality with same email, name and password
        final Customer jonathanDupe = new Customer( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        service.save( jonathanDupe );
        assertNotEquals( jonathan, jonathanDupe );

        // Check equality with same name and password
        final Customer jonathanDupe2 = new Customer( "jonathan2@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        service.save( jonathanDupe2 );
        assertNotEquals( jonathan, jonathanDupe2 );

    }

    /**
     * Tests updating employee
     *
     * @throws InvalidAttributeValueException
     *             if input is invalid
     */
    @Test
    @Transactional
    public void testUpdate () throws InvalidAttributeValueException {
        final Customer jonathan = new Customer( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        // Check that fields were set properly
        assertEquals( "jonathan@gmail.com", jonathan.getEmail() );
        assertEquals( "Jonathan Kurian", jonathan.getName() );
        assertEquals( "jonathan'spassword", jonathan.getPassword() );

        service.save( jonathan );

        final long id = (long) jonathan.getId();

        // Make duplicate with different email, name, and password
        final Customer jonathanUpdate = new Customer( "jon@gmail.com", "Jon Kurian", "jon'spassword" );
        // Update
        jonathan.updateUser( jonathanUpdate );
        // Check fields updated appropriately - email should not change
        assertEquals( "jonathan@gmail.com", jonathan.getEmail() );
        assertEquals( "Jon Kurian", jonathan.getName() );
        assertEquals( "jon'spassword", jonathan.getPassword() );
        assertEquals( id, jonathan.getId() ); // id should not change

        // Save
        service.save( jonathan );

        // Retrieve Jonathan from service
        final Customer serviceJonUpdated = service.findById( id );
        // Check fields updated appropriately - email should not change
        assertEquals( "jonathan@gmail.com", serviceJonUpdated.getEmail() );
        assertEquals( "Jon Kurian", serviceJonUpdated.getName() );
        assertEquals( "jon'spassword", serviceJonUpdated.getPassword() );
        assertEquals( id, serviceJonUpdated.getId() ); // id should not change

        // Check that only 1 Jonathan exists
        assertEquals( 1, service.count() );
    }

    /**
     * Tests error checking for invalid input
     */
    @Test
    @Transactional
    public void testInvalidInput () {

        // Empty email
        try {
            final Customer erin = new Customer( "", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Empty name
        try {
            final Customer erin = new Customer( "erin@gmail.com", "", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Empty password
        try {
            final Customer erin = new Customer( "erin@gmail.com", "Erin Grouge", "" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Null email
        try {
            final Customer erin = new Customer( null, "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Null email
        try {
            final Customer erin = new Customer( "erin@gmail.com", null, "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        // Null email
        try {
            final Customer erin = new Customer( "erin@gmail.com", "Erin Grouge", null );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }

        // Check email formatting
        try {
            final Customer erin = new Customer( "erin@gmail", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( "erin@gmail.", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( "erin@.com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( "erin@.", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( "erin.com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( "erin.gmail@com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( "erin.@com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( "erin.@", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( ".@", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }
        try {
            final Customer erin = new Customer( "@gmail.com", "Erin Grouge", "erin'spassword" );
            fail();
        }
        catch ( final InvalidAttributeValueException e ) {
            assertEquals( "Invalid Input", e.getMessage() );
        }

    }
}
