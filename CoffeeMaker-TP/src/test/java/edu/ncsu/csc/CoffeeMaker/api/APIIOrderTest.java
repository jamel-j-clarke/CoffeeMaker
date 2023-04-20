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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.users.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

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
     * Stores and saves the recipes
     */
    @Autowired
    private RecipeService         recService;

    /**
     * Stores and saves the customers
     */
    @Autowired
    private CustomerService       customerService;

    /**
     * Stores and saves the recipes
     */
    @Autowired
    private InventoryService      inventoryService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Chocolate", 500 ) );
        ivt.addIngredient( new Ingredient( "Milk", 500 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 500 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 500 ) );

        inventoryService.save( ivt );
        service.deleteAll();
    }

    /**
     * Tests the post API endpoint
     */
    @Test
    @Transactional
    public void testPost () throws Exception {

        final Customer cust1 = new Customer( "cust1@gmail.com", "Cust 1", "password1" );
        customerService.save( cust1 );

        final Recipe r1 = new Recipe( "Mocha", 10 );
        r1.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 4 ) );
        r1.addIngredient( new Ingredient( "Sugar", 8 ) );
        recService.save( r1 );
        // Post Order 1
        final Order o1 = new Order( r1.getName(), 10, cust1.getEmail() );

        final String strid1 = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( o1 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final long id1 = Long.parseLong( strid1 );

        // Check that it is saved in service
        assertEquals( 1, service.count() );

        // Retrieve the contents with a get request
        String ord = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( ord.contains( "cust1@gmail.com" ) );
        assertTrue( ord.contains( "Mocha" ) );

        final Customer cust2 = new Customer( "cust2@gmail.com", "Cust 2", "password2" );
        customerService.save( cust2 );

        final Recipe r2 = new Recipe( "Cinnamon", 2 );
        r2.addIngredient( new Ingredient( "Chocolate", 12 ) );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 4 ) );
        r2.addIngredient( new Ingredient( "Sugar", 8 ) );
        recService.save( r2 );
        // Post Order 1
        final Order o2 = new Order( r2.getName(), 2, cust2.getEmail() );

        final String strid2 = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( o2 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final long id2 = Long.parseLong( strid2 );

        // Check that it is saved in service
        assertEquals( 2, service.count() );

        // Retrieve the contents with a get request
        ord = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( ord.contains( "cust2@gmail.com" ) );
        assertTrue( ord.contains( "Cinnamon" ) );

        // Post Order 3
        final Order order3 = new Order( r1.getName(), 10, cust2.getEmail() );
        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order3 ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 3, service.count() );

        // Retrieve the contents with a get request
        ord = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( ord.contains( "Cinnamon" ) );
        assertTrue( ord.contains( "Mocha" ) );

        ord = mvc.perform( get( "/api/v1/orders/customer/" + cust1.getEmail() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( ord.contains( "Mocha" ) );
        assertFalse( ord.contains( "Cinnamon" ) );

        ord = mvc.perform( get( "/api/v1/orders/customer/" + cust2.getEmail() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( ord.contains( "Mocha" ) );
        assertTrue( ord.contains( "Cinnamon" ) );

    }

    /**
     * Tests the put API endpoints
     */
    @Test
    @Transactional
    public void testPut () throws Exception {
        service.deleteAll();
        // Create an Order
        final Customer cust1 = new Customer( "cust1@gmail.com", "Cust 1", "password1" );
        customerService.save( cust1 );

        final Recipe r1 = new Recipe( "Mocha", 10 );
        r1.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 4 ) );
        r1.addIngredient( new Ingredient( "Sugar", 8 ) );
        recService.save( r1 );
        // Post Order 1
        final Order o1 = new Order( r1.getName(), 10, cust1.getEmail() );

        final String strid1 = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( o1 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final long id1 = Long.parseLong( strid1 );

        String orders = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( orders.contains( "Mocha" ) );
        assertEquals( 1, service.count() );

        orders = mvc.perform( get( "/api/v1/orders/customer/" + cust1.getEmail() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        System.out.println( "*** Orders: " + orders );
        assertTrue( orders.contains( "" + id1 ) );

        String incOrders = mvc.perform( get( "/api/v1/orders/incomplete" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( incOrders.contains( "Mocha" ) );
        String inpOrders = mvc.perform( get( "/api/v1/orders/inprogress" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertFalse( inpOrders.contains( "Mocha" ) );
        String compOrders = mvc.perform( get( "/api/v1/orders/completed" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertFalse( compOrders.contains( "Mocha" ) );
        String picOrders = mvc.perform( get( "/api/v1/orders/pickedup" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( picOrders.contains( "Mocha" ) );

        // Start Order
        mvc.perform( put( "/api/v1/orders/start/" + id1 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        incOrders = mvc.perform( get( "/api/v1/orders/incomplete" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( incOrders.contains( "Mocha" ) );
        inpOrders = mvc.perform( get( "/api/v1/orders/inprogress" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( inpOrders.contains( "Mocha" ) );
        compOrders = mvc.perform( get( "/api/v1/orders/completed" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( compOrders.contains( "Mocha" ) );
        picOrders = mvc.perform( get( "/api/v1/orders/pickedup" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( picOrders.contains( "Mocha" ) );

        // Complete Order
        mvc.perform( put( "/api/v1/orders/complete/" + id1 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        incOrders = mvc.perform( get( "/api/v1/orders/incomplete" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( incOrders.contains( "Mocha" ) );
        inpOrders = mvc.perform( get( "/api/v1/orders/inprogress" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( inpOrders.contains( "Mocha" ) );
        compOrders = mvc.perform( get( "/api/v1/orders/completed" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( compOrders.contains( "Mocha" ) );
        picOrders = mvc.perform( get( "/api/v1/orders/pickedup" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( picOrders.contains( "Mocha" ) );

        // Pick Up Order
        mvc.perform( put( "/api/v1/orders/pickup/" + id1 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        incOrders = mvc.perform( get( "/api/v1/orders/incomplete" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( incOrders.contains( "Mocha" ) );
        inpOrders = mvc.perform( get( "/api/v1/orders/inprogress" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( inpOrders.contains( "Mocha" ) );
        compOrders = mvc.perform( get( "/api/v1/orders/completed" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( compOrders.contains( "Mocha" ) );
        picOrders = mvc.perform( get( "/api/v1/orders/pickedup" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( picOrders.contains( "Mocha" ) );

        // Check for orders that do not exist
        mvc.perform( put( "/api/v1/orders/start/123456" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
        mvc.perform( put( "/api/v1/orders/complete/123456" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
        mvc.perform( put( "/api/v1/orders/pickup/123456" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );

        // Check invalid routes

        final Customer cust2 = new Customer( "cust2@gmail.com", "Cust 2", "password2" );
        customerService.save( cust2 );

        final Recipe r2 = new Recipe( "Cinnamon", 2 );
        r2.addIngredient( new Ingredient( "Chocolate", 12 ) );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 4 ) );
        r2.addIngredient( new Ingredient( "Sugar", 8 ) );
        recService.save( r2 );
        // Post Order 1
        final Order o2 = new Order( r2.getName(), 2, cust2.getEmail() );

        final String strid2 = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( o2 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final long id2 = Long.parseLong( strid2 );

        // Cannot be completed bc order has not been started
        mvc.perform( put( "/api/v1/orders/complete/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );
        mvc.perform( put( "/api/v1/orders/pickup/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );

        // Start order
        mvc.perform( put( "/api/v1/orders/start/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Cannot be completed bc order has not been completed
        mvc.perform( put( "/api/v1/orders/start/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );
        mvc.perform( put( "/api/v1/orders/pickup/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );

        // Complete order
        mvc.perform( put( "/api/v1/orders/complete/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Cannot be completed bc order is ready for pickup
        mvc.perform( put( "/api/v1/orders/start/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );
        mvc.perform( put( "/api/v1/orders/complete/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );

    }

    /**
     * Tests the delete API endpoints
     */
    @Test
    @Transactional
    public void testCancel () throws Exception {
        service.deleteAll();
        // Create Orders
        final Customer cust1 = new Customer( "cust1@gmail.com", "Cust 1", "password1" );
        customerService.save( cust1 );

        final Recipe r1 = new Recipe( "Mocha", 10 );
        r1.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 4 ) );
        r1.addIngredient( new Ingredient( "Sugar", 8 ) );
        recService.save( r1 );
        // Post Order 1
        final Order o1 = new Order( r1.getName(), 10, cust1.getEmail() );

        String strid1 = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( o1 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        long id1 = Long.parseLong( strid1 );

        final Customer cust2 = new Customer( "cust2@gmail.com", "Cust 2", "password2" );
        customerService.save( cust2 );

        final Recipe r2 = new Recipe( "Cinnamon", 2 );
        r2.addIngredient( new Ingredient( "Chocolate", 12 ) );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 4 ) );
        r2.addIngredient( new Ingredient( "Sugar", 8 ) );
        recService.save( r2 );
        // Post Order 1
        final Order o2 = new Order( r2.getName(), 2, cust2.getEmail() );

        final String strid2 = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( o2 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final long id2 = Long.parseLong( strid2 );

        assertEquals( 2, service.count() );

        // Delete order1 because it has not been started.
        mvc.perform( delete( "/api/v1/orders/customer/" + id1 ) ).andExpect( status().isOk() );

        assertEquals( 1, service.count() );

        // Start order 2
        mvc.perform( put( "/api/v1/orders/start/" + id2 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Customer cannot delete started order.
        mvc.perform( delete( "/api/v1/orders/customer/" + id2 ) ).andExpect( status().isNotAcceptable() );
        assertEquals( 1, service.count() );

        // But an employee can
        mvc.perform( delete( "/api/v1/orders/employee/" + id2 ) ).andExpect( status().isOk() );

        // But not a completed or picked up order
        strid1 = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( o1 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        id1 = Long.parseLong( strid1 );
        assertEquals( 1, service.count() );

        mvc.perform( put( "/api/v1/orders/start/" + id1 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        mvc.perform( put( "/api/v1/orders/complete/" + id1 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Will not work
        mvc.perform( delete( "/api/v1/orders/employee/" + id1 ) ).andExpect( status().isNotAcceptable() );
        assertEquals( 1, service.count() );

        mvc.perform( put( "/api/v1/orders/pickup/" + id1 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Will not work
        mvc.perform( delete( "/api/v1/orders/employee/" + id1 ) ).andExpect( status().isNotAcceptable() );
        assertEquals( 1, service.count() );

        // Deleting an order that does not exist
        mvc.perform( delete( "/api/v1/orders/employee/123456" ) ).andExpect( status().isNotFound() );
        mvc.perform( delete( "/api/v1/orders/customer/123456" ) ).andExpect( status().isNotFound() );

    }

}
