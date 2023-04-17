package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.users.Customer;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This tests the Order class and its methods.
 *
 * @author Erin Grouge
 * @author Emma Holincheck
 *
 * @version 04/17/2023
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class OrderTest {

    /** The service object used to store the orders */
    @Autowired
    private OrderService  service;

    /** The service object used to store the recipes */
    @Autowired
    private RecipeService recService;

    /**
     * Deletes all previously stored recipes before each test
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
        recService.deleteAll();
        final Recipe mocha = new Recipe( "Mocha", 8 );
        mocha.addIngredient( new Ingredient( "Coffee", 6 ) );
        mocha.addIngredient( new Ingredient( "Milk", 6 ) );
        mocha.addIngredient( new Ingredient( "Sugar", 6 ) );

        final Recipe coffee = new Recipe( "Coffee", 4 );
        coffee.addIngredient( new Ingredient( "Coffee", 6 ) );

        final Recipe chai = new Recipe( "Chai Latte", 4 );
        chai.addIngredient( new Ingredient( "Chai", 6 ) );
        chai.addIngredient( new Ingredient( "Milk", 6 ) );

        recService.save( mocha );
        recService.save( coffee );
        recService.save( chai );
    }

    /**
     * Tests creating and saving orders.
     *
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     * @throws InvalidAttributeValueException
     *             if there is an error
     */
    @Test
    @Transactional
    public void testOrder () throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        assertEquals( 0, service.count() );

        final Recipe mocha = recService.findByName( "Mocha" );
        final Order order1 = new Order( "Mocha", 8, "etgrouge@ncsu.edu" );
        assertEquals( OrderStatus.NOT_STARTED, order1.getStatus() );
        assertEquals( 8, order1.getPayment() );
        assertEquals( "etgrouge@ncsu.edu", order1.getUserEmail() );

        service.save( order1 );
        assertEquals( 1, service.count() );

        // Check copy saved in service.
        Order serviceOrder1 = service.findById( (Long) order1.getId() );
        assertEquals( OrderStatus.NOT_STARTED, serviceOrder1.getStatus() );
        assertEquals( 8, serviceOrder1.getPayment() );
        assertEquals( "etgrouge@ncsu.edu", serviceOrder1.getUserEmail() );

        order1.start();
        assertEquals( OrderStatus.IN_PROGRESS, order1.getStatus() );
        service.save( order1 );

        serviceOrder1 = service.findById( (Long) order1.getId() );
        assertEquals( OrderStatus.IN_PROGRESS, serviceOrder1.getStatus() );

        order1.complete();
        assertEquals( OrderStatus.DONE, order1.getStatus() );
        service.save( order1 );

        serviceOrder1 = service.findById( (Long) order1.getId() );
        assertEquals( OrderStatus.DONE, serviceOrder1.getStatus() );

        order1.pickup();
        assertEquals( OrderStatus.PICKED_UP, order1.getStatus() );
        service.save( order1 );

        serviceOrder1 = service.findById( (Long) order1.getId() );
        assertEquals( OrderStatus.PICKED_UP, serviceOrder1.getStatus() );

        // Order 2
        final Recipe chai = recService.findByName( "Chai" );
        final Order order2 = new Order( "Chai", 7, "egrouge@ncsu.edu" );
        assertEquals( OrderStatus.NOT_STARTED, order2.getStatus() );
        assertEquals( 7, order2.getPayment() );
        assertEquals( "egrouge@ncsu.edu", order2.getUserEmail() );

        service.save( order2 );
        assertEquals( 2, service.count() );

        // Check copy saved in service.
        Order serviceOrder2 = service.findById( (Long) order2.getId() );
        assertEquals( OrderStatus.NOT_STARTED, serviceOrder2.getStatus() );
        assertEquals( 7, serviceOrder2.getPayment() );
        assertEquals( "egrouge@ncsu.edu", serviceOrder2.getUserEmail() );

        order2.cancel();
        assertEquals( OrderStatus.CANCELLED, order2.getStatus() );

        service.save( order2 );
        serviceOrder2 = service.findById( (Long) order2.getId() );
        assertEquals( OrderStatus.CANCELLED, serviceOrder2.getStatus() );

    }

    /**
     * Tests customer's orders
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
    public void testCustomerOrders ()
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        final Recipe mocha = recService.findByName( "Mocha" );
        final Order order1 = new Order( "Mocha", 8, "etgrouge@ncsu.edu" );

        final Recipe chai = recService.findByName( "Chai" );
        final Order order2 = new Order( "Chai", 7, "etgrouge@ncsu.edu" );

        // Not the same user
        final Recipe coffee = recService.findByName( "Coffee" );
        final Order order3 = new Order( "Coffee", 7, "egrouge@ncsu.edu" );

        final Customer customer = new Customer( "etgrouge@ncsu.edu", "Erin", "password" );
        assertEquals( 0, customer.getOrders().size() );

        // Order Beverage 1
        assertTrue( customer.orderBeverage( order1 ) );

        assertEquals( 1, customer.getOrders().size() );
        assertEquals( order1, customer.getOrders().get( 0 ) );

        // Order Beverage 2
        assertTrue( customer.orderBeverage( order2 ) );

        assertEquals( 2, customer.getOrders().size() );
        assertEquals( order1, customer.getOrders().get( 0 ) );
        assertEquals( order2, customer.getOrders().get( 1 ) );

        // Not the same email
        assertFalse( customer.orderBeverage( order3 ) );

        // Cancel Order
        assertTrue( customer.cancelOrder( order1 ) );

        assertEquals( 1, customer.getOrders().size() );
        assertEquals( order2, customer.getOrders().get( 0 ) );
    }

    /**
     * Tests the Equals method in Order to ensure the correct logic was
     * implemented
     */
    @Test
    @Transactional
    public void testEquivalentOrders () {
        final Order order1 = new Order( "Coffee", 7, "echolinc@ncsu.edu" );
        final Order order2 = new Order( "Coffee", 7, "echolinc@ncsu.edu" );
        // Test null case in the equals method
        final Order order3 = new Order( null, null, null );
        final Order order4 = new Order( "Mocha", 7, "egrouge@ncsu.edu" );
        final Order order5 = new Order( "Mocha", 9, "egrouge@ncsu.edu" );
        final Order order6 = new Order( "Mocha", 9, "echolinc@ncsu.edu" );

        // Assert that two object that have yet to be committed to the database
        // are considered equal
        assertTrue( order1.equals( order2 ) );
        assertTrue( order2.equals( order1 ) );

        // Tests that a null order returns false for equals
        assertFalse( order1.equals( order3 ) );
        // Tests that name of beverage differences return false for equals
        assertFalse( order2.equals( order4 ) );
        // Test that payment differences return false for equals
        assertFalse( order4.equals( order5 ) );
        // Tests that email differences return false for equals
        assertFalse( order5.equals( order6 ) );
        // Save the orders to the repository to then test that the equals method
        // will work when comparing identical order contents placed at different
        // times by the same user
        service.save( order1 );
        service.save( order2 );
        final Order order1B = service.findById( (long) order1.getId() );
        final Order order2B = service.findById( (long) order2.getId() );
        assertFalse( order1B.equals( order2B ) );
        assertFalse( order2B.equals( order1B ) );

    }
}
