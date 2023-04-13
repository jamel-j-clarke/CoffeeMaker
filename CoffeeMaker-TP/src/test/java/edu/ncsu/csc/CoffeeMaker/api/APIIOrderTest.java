package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.UserInfo;
import edu.ncsu.csc.CoffeeMaker.models.users.Customer;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 * Tests the APIOrderController class and its methods.
 *
 * @author Jonathan Kurian
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /**
     * Used to build the mvc
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Stores and saves the orders
     */
    @Autowired
    private OrderService          service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Tests the post API endpoint
     */
    @Test
    @Transactional
    public void testPost () throws Exception {

        final Customer cust1 = new Customer( "cust1@gmail.com", "Cust 1", "password1" );

        final Recipe r1 = new Recipe( "Mocha", 10 );
        r1.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 4 ) );
        r1.addIngredient( new Ingredient( "Sugar", 8 ) );
        // Post Order 1
        final Order o1 = new Order( r1, 12, cust1 );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 1, service.count() );

        // Retrieve the contents with a get request
        String ord = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( ord.contains( "Cust 1" ) );

        final Customer cust2 = new Customer( "cust2@gmail.com", "Cust 1", "password1" );

        final Recipe r2 = new Recipe( "Cinnamon", 2 );
        r2.addIngredient( new Ingredient( "Chocolate", 12 ) );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 4 ) );
        r2.addIngredient( new Ingredient( "Sugar", 8 ) );
        // Post Order 1
        final Order o2 = new Order( r2, 12, cust2 );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o2 ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 2, service.count() );

        // Retrieve the contents with a get request
        ord = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( ord.contains( "Cust 2" ) );

        // Post Order 3
        final Order jonathan = new Order( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( jonathan ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 3, service.count() );

        // Retrieve the contents with a get request
        cust = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( cust.contains( "Jonathan Kurian" ) );
        assertTrue( cust.contains( "jonathan@gmail.com" ) );

        // Check that Order with duplicate email will not be created.
        final Order jonathanDupe = new Order( "jonathan@gmail.com", "Jon Kurian", "jon'spassword" );
        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( jonathanDupe ) ) ).andExpect( status().isConflict() );

        // Check that there is still only 3
        assertEquals( 3, service.count() );
        cust = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( cust.contains( "Jonathan Kurian" ) );
        assertTrue( cust.contains( "jonathan@gmail.com" ) );
        assertFalse( cust.contains( "Jon Kurian" ) );

    }

    /**
     * Tests the put API endpoints
     */
    @Test
    @Transactional
    public void testPut () throws Exception {
        service.deleteAll();
        // Create an Order
        final Order cust = new Order( "order@gmail.com", "Mrs. Order", "custpassword" );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust ) ) ).andExpect( status().isOk() );

        String orders = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( orders.contains( "order@gmail.com" ) );
        assertTrue( orders.contains( "Mrs. Order" ) );
        assertEquals( 1, service.count() );

        final Long id = (Long) service.findByEmail( "order@gmail.com" ).getId();

        // Update the Order - change name
        final Order custUpdateName = new Order( "order@gmail.com", "Ms. Order", "custpassword" );

        mvc.perform( put( "/api/v1/orders/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( custUpdateName ) ) ).andExpect( status().isOk() );

        orders = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( orders.contains( "order@gmail.com" ) );
        assertTrue( orders.contains( "Ms. Order" ) );
        assertFalse( orders.contains( "Mrs. Order" ) );
        assertTrue( orders.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Update the Order - change password
        final Order custUpdatePassword = new Order( "order@gmail.com", "Ms. Order", "custnewpassword" );

        mvc.perform( put( "/api/v1/orders/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( custUpdatePassword ) ) ).andExpect( status().isOk() );
        orders = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( orders.contains( "order@gmail.com" ) );
        assertTrue( orders.contains( "Ms. Order" ) );
        assertTrue( orders.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Update the Order - change email (Not allowed)
        final Order custUpdateEmail = new Order( "cust@gmail.com", "Ms. Order", "custnewpassword" );

        mvc.perform( put( "/api/v1/orders/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( custUpdateEmail ) ) ).andExpect( status().isOk() );

        orders = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( orders.contains( "order@gmail.com" ) );
        assertTrue( orders.contains( "Ms. Order" ) );
        assertFalse( orders.contains( "cust@gmail.com" ) ); // Cannot change
                                                            // email
        assertTrue( orders.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Test putting with an id that does not exist
        mvc.perform( put( "/api/v1/orders/12345" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( custUpdateEmail ) ) ).andExpect( status().isNotFound() );

    }

    /**
     * Tests the get API endpoints
     */
    @Test
    @Transactional
    public void testGet () throws Exception {
        service.deleteAll();
        // Create Orders
        final Order cust1 = new Order( "order1@gmail.com", "Mrs. Order", "cust1password" );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust1 ) ) ).andExpect( status().isOk() );

        assertEquals( 1, service.count() );
        String employees = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        System.out.println( "****" + employees );
        assertTrue( employees.contains( "order1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Order" ) );
        assertFalse( employees.contains( "order2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Order" ) );
        assertFalse( employees.contains( "order3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Order" ) );

        final Order cust2 = new Order( "order2@gmail.com", "Ms. Order", "cust2password" );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust2 ) ) ).andExpect( status().isOk() );

        assertEquals( 2, service.count() );
        employees = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "order1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Order" ) );
        assertTrue( employees.contains( "order2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Order" ) );
        assertFalse( employees.contains( "order3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Order" ) );

        final Order cust3 = new Order( "order3@gmail.com", "Mr. Order", "cust3password" );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust3 ) ) ).andExpect( status().isOk() );

        assertEquals( 3, service.count() );
        employees = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "order1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Order" ) );
        assertTrue( employees.contains( "order2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Order" ) );
        assertTrue( employees.contains( "order3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Order" ) );

        employees = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Get Orders by ID
        // Order 1
        String emp1str = mvc.perform( get( "/api/v1/orders/" + service.findByEmail( "order1@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertTrue( emp1str.contains( "order1@gmail.com" ) );
        assertTrue( emp1str.contains( "Mrs. Order" ) );
        assertTrue( emp1str.contains( "" + service.findByEmail( "order1@gmail.com" ).getId() ) );
        assertFalse( emp1str.contains( "order2@gmail.com" ) );
        assertFalse( emp1str.contains( "Ms. Order" ) );
        assertFalse( emp1str.contains( "order3@gmail.com" ) );
        assertFalse( emp1str.contains( "Mr. Order" ) );

        // Order 2
        String emp2str = mvc.perform( get( "/api/v1/orders/" + service.findByEmail( "order2@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp2str.contains( "order1@gmail.com" ) );
        assertFalse( emp2str.contains( "Mrs. Order" ) );
        assertTrue( emp2str.contains( "order2@gmail.com" ) );
        assertTrue( emp2str.contains( "Ms. Order" ) );
        assertTrue( emp2str.contains( "" + service.findByEmail( "order2@gmail.com" ).getId() ) );
        assertFalse( emp2str.contains( "order3@gmail.com" ) );
        assertFalse( emp2str.contains( "Mr. Order" ) );

        // Order 3
        String emp3str = mvc.perform( get( "/api/v1/orders/" + service.findByEmail( "order3@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp3str.contains( "order1@gmail.com" ) );
        assertFalse( emp3str.contains( "Mrs. Order" ) );
        assertFalse( emp3str.contains( "order2@gmail.com" ) );
        assertFalse( emp3str.contains( "Ms. Order" ) );
        assertTrue( emp3str.contains( "order3@gmail.com" ) );
        assertTrue( emp3str.contains( "Mr. Order" ) );
        assertTrue( emp3str.contains( "" + service.findByEmail( "order3@gmail.com" ).getId() ) );

        // Test Getting an Order that does not exist
        mvc.perform( get( "/api/v1/orders/12345" ) ).andDo( print() ).andExpect( status().isNotFound() );

        // Get Order by Email
        // Order 1
        emp1str = mvc.perform( get( "/api/v1/orders/email/order1@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertTrue( emp1str.contains( "order1@gmail.com" ) );
        assertTrue( emp1str.contains( "Mrs. Order" ) );
        assertFalse( emp1str.contains( "order2@gmail.com" ) );
        assertFalse( emp1str.contains( "Ms. Order" ) );
        assertFalse( emp1str.contains( "order3@gmail.com" ) );
        assertFalse( emp1str.contains( "Mr. Order" ) );

        // Order 2
        emp2str = mvc.perform( get( "/api/v1/orders/email/order2@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp2str.contains( "order1@gmail.com" ) );
        assertFalse( emp2str.contains( "Mrs. Order" ) );
        assertTrue( emp2str.contains( "order2@gmail.com" ) );
        assertTrue( emp2str.contains( "Ms. Order" ) );
        assertFalse( emp2str.contains( "order3@gmail.com" ) );
        assertFalse( emp2str.contains( "Mr. Order" ) );

        // Order 3
        emp3str = mvc.perform( get( "/api/v1/orders/email/order3@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp3str.contains( "order1@gmail.com" ) );
        assertFalse( emp3str.contains( "Mrs. Order" ) );
        assertFalse( emp3str.contains( "order2@gmail.com" ) );
        assertFalse( emp3str.contains( "Ms. Order" ) );
        assertTrue( emp3str.contains( "order3@gmail.com" ) );
        assertTrue( emp3str.contains( "Mr. Order" ) );

        // Test Getting an Order that does not exist
        mvc.perform( get( "/api/v1/orders/email/email@gmail.com" ) ).andDo( print() )
                .andExpect( status().isNotFound() );

    }

    /**
     * Tests the delete API endpoints
     */
    @Test
    @Transactional
    public void testDelete () throws Exception {
        service.deleteAll();
        // Create Orders
        final Order cust1 = new Order( "order1@gmail.com", "Mrs. Order", "cust1password" );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust1 ) ) ).andExpect( status().isOk() );

        assertEquals( 1, service.count() );

        final Order cust2 = new Order( "order2@gmail.com", "Ms. Order", "cust2password" );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust2 ) ) ).andExpect( status().isOk() );

        assertEquals( 2, service.count() );

        final Order cust3 = new Order( "order3@gmail.com", "Mr. Order", "cust3password" );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust3 ) ) ).andExpect( status().isOk() );

        assertEquals( 3, service.count() );
        String employees = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( employees.contains( "order1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Order" ) );
        assertTrue( employees.contains( "order2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Order" ) );
        assertTrue( employees.contains( "order3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Order" ) );

        // Test Delete Employee 2
        mvc.perform( delete( "/api/v1/orders/" + service.findByEmail( "order2@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 2, service.count() );
        employees = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "order1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Order" ) );
        assertFalse( employees.contains( "order2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Order" ) );
        assertTrue( employees.contains( "order3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Order" ) );

        // Test Delete Order 1
        mvc.perform( delete( "/api/v1/orders/" + service.findByEmail( "order1@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 1, service.count() );
        employees = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( employees.contains( "order1@gmail.com" ) );
        assertFalse( employees.contains( "Mrs. Order" ) );
        assertFalse( employees.contains( "order2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Order" ) );
        assertTrue( employees.contains( "order3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Order" ) );

        // Test Delete Order 3
        mvc.perform( delete( "/api/v1/orders/" + service.findByEmail( "order3@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 0, service.count() );
        employees = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( employees.contains( "order1@gmail.com" ) );
        assertFalse( employees.contains( "Mrs. Order" ) );
        assertFalse( employees.contains( "order2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Order" ) );
        assertFalse( employees.contains( "order3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Order" ) );

        // Try Deleting an employee that doesn't exist
        mvc.perform( delete( "/api/v1/orders/12345" ) ).andExpect( status().isNotFound() );

    }

    /**
     * Tests validating a user's email and password
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testValidateOrder () throws Exception {
        final UserInfo order = new UserInfo( "order@gmail.com", "Order", "password" );
        System.out.println( "***Pass after test construction: " + order.getPassword() );
        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order ) ) ).andExpect( status().isOk() );

        final UserInfo attempt = new UserInfo( "order@gmail.com", null, "password" );
        mvc.perform( post( "/api/v1/orders/validate" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( attempt ) ) ).andExpect( status().isOk() );
    }

}
