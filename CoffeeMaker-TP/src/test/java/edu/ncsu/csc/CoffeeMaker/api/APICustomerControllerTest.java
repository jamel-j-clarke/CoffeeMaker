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
import edu.ncsu.csc.CoffeeMaker.models.UserInfo;
import edu.ncsu.csc.CoffeeMaker.models.users.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;

/**
 * Tests the APICustomerController class and its methods.
 *
 * @author Erin Grouge
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APICustomerControllerTest {

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
     * Stores and saves the recipes
     */
    @Autowired
    private CustomerService       service;

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
        // Post Customer 1
        final Customer erin = new Customer( "erin@gmail.com", "Erin Grouge", "erin'spassword" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( erin ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 1, service.count() );

        // Retrieve the contents with a get request
        String cust = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( cust.contains( "Erin Grouge" ) );
        assertTrue( cust.contains( "erin@gmail.com" ) );

        // Post Customer 2
        final Customer emma = new Customer( "emma@gmail.com", "Emma Holincheck", "emma'spassword" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emma ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 2, service.count() );

        // Retrieve the contents with a get request
        cust = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( cust.contains( "Emma Holincheck" ) );
        assertTrue( cust.contains( "emma@gmail.com" ) );

        // Post Customer 3
        final Customer jonathan = new Customer( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( jonathan ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 3, service.count() );

        // Retrieve the contents with a get request
        cust = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( cust.contains( "Jonathan Kurian" ) );
        assertTrue( cust.contains( "jonathan@gmail.com" ) );

        // Check that Customer with duplicate email will not be created.
        final Customer jonathanDupe = new Customer( "jonathan@gmail.com", "Jon Kurian", "jon'spassword" );
        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( jonathanDupe ) ) ).andExpect( status().isConflict() );

        // Check that there is still only 3
        assertEquals( 3, service.count() );
        cust = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
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
        // Create an Customer
        final Customer cust = new Customer( "customer@gmail.com", "Mrs. Customer", "custpassword" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust ) ) ).andExpect( status().isOk() );

        String customers = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( customers.contains( "customer@gmail.com" ) );
        assertTrue( customers.contains( "Mrs. Customer" ) );
        assertEquals( 1, service.count() );

        final Long id = (Long) service.findByEmail( "customer@gmail.com" ).getId();

        // Update the Customer - change name
        final Customer custUpdateName = new Customer( "customer@gmail.com", "Ms. Customer", "custpassword" );

        mvc.perform( put( "/api/v1/customers/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( custUpdateName ) ) ).andExpect( status().isOk() );

        customers = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( customers.contains( "customer@gmail.com" ) );
        assertTrue( customers.contains( "Ms. Customer" ) );
        assertFalse( customers.contains( "Mrs. Customer" ) );
        assertTrue( customers.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Update the Customer - change password
        final Customer custUpdatePassword = new Customer( "customer@gmail.com", "Ms. Customer", "custnewpassword" );

        mvc.perform( put( "/api/v1/customers/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( custUpdatePassword ) ) ).andExpect( status().isOk() );
        customers = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( customers.contains( "customer@gmail.com" ) );
        assertTrue( customers.contains( "Ms. Customer" ) );
        assertTrue( customers.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Update the Customer - change email (Not allowed)
        final Customer custUpdateEmail = new Customer( "cust@gmail.com", "Ms. Customer", "custnewpassword" );

        mvc.perform( put( "/api/v1/customers/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( custUpdateEmail ) ) ).andExpect( status().isOk() );

        customers = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( customers.contains( "customer@gmail.com" ) );
        assertTrue( customers.contains( "Ms. Customer" ) );
        assertFalse( customers.contains( "cust@gmail.com" ) ); // Cannot change
                                                               // email
        assertTrue( customers.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Test putting with an id that does not exist
        mvc.perform( put( "/api/v1/customers/12345" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( custUpdateEmail ) ) ).andExpect( status().isNotFound() );

    }

    /**
     * Tests the get API endpoints
     */
    @Test
    @Transactional
    public void testGet () throws Exception {
        service.deleteAll();
        // Create Customers
        final Customer cust1 = new Customer( "customer1@gmail.com", "Mrs. Customer", "cust1password" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust1 ) ) ).andExpect( status().isOk() );

        assertEquals( 1, service.count() );
        String employees = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        System.out.println( "****" + employees );
        assertTrue( employees.contains( "customer1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Customer" ) );
        assertFalse( employees.contains( "customer2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Customer" ) );
        assertFalse( employees.contains( "customer3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Customer" ) );

        final Customer cust2 = new Customer( "customer2@gmail.com", "Ms. Customer", "cust2password" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust2 ) ) ).andExpect( status().isOk() );

        assertEquals( 2, service.count() );
        employees = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "customer1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Customer" ) );
        assertTrue( employees.contains( "customer2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Customer" ) );
        assertFalse( employees.contains( "customer3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Customer" ) );

        final Customer cust3 = new Customer( "customer3@gmail.com", "Mr. Customer", "cust3password" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust3 ) ) ).andExpect( status().isOk() );

        assertEquals( 3, service.count() );
        employees = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "customer1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Customer" ) );
        assertTrue( employees.contains( "customer2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Customer" ) );
        assertTrue( employees.contains( "customer3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Customer" ) );

        employees = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Get Customers by ID
        // Customer 1
        String emp1str = mvc
                .perform( get( "/api/v1/customers/" + service.findByEmail( "customer1@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertTrue( emp1str.contains( "customer1@gmail.com" ) );
        assertTrue( emp1str.contains( "Mrs. Customer" ) );
        assertTrue( emp1str.contains( "" + service.findByEmail( "customer1@gmail.com" ).getId() ) );
        assertFalse( emp1str.contains( "customer2@gmail.com" ) );
        assertFalse( emp1str.contains( "Ms. Customer" ) );
        assertFalse( emp1str.contains( "customer3@gmail.com" ) );
        assertFalse( emp1str.contains( "Mr. Customer" ) );

        // Customer 2
        String emp2str = mvc
                .perform( get( "/api/v1/customers/" + service.findByEmail( "customer2@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp2str.contains( "customer1@gmail.com" ) );
        assertFalse( emp2str.contains( "Mrs. Customer" ) );
        assertTrue( emp2str.contains( "customer2@gmail.com" ) );
        assertTrue( emp2str.contains( "Ms. Customer" ) );
        assertTrue( emp2str.contains( "" + service.findByEmail( "customer2@gmail.com" ).getId() ) );
        assertFalse( emp2str.contains( "customer3@gmail.com" ) );
        assertFalse( emp2str.contains( "Mr. Customer" ) );

        // Customer 3
        String emp3str = mvc
                .perform( get( "/api/v1/customers/" + service.findByEmail( "customer3@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp3str.contains( "customer1@gmail.com" ) );
        assertFalse( emp3str.contains( "Mrs. Customer" ) );
        assertFalse( emp3str.contains( "customer2@gmail.com" ) );
        assertFalse( emp3str.contains( "Ms. Customer" ) );
        assertTrue( emp3str.contains( "customer3@gmail.com" ) );
        assertTrue( emp3str.contains( "Mr. Customer" ) );
        assertTrue( emp3str.contains( "" + service.findByEmail( "customer3@gmail.com" ).getId() ) );

        // Test Getting an Customer that does not exist
        mvc.perform( get( "/api/v1/customers/12345" ) ).andDo( print() ).andExpect( status().isNotFound() );

        // Get Customer by Email
        // Customer 1
        emp1str = mvc.perform( get( "/api/v1/customers/email/customer1@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertTrue( emp1str.contains( "customer1@gmail.com" ) );
        assertTrue( emp1str.contains( "Mrs. Customer" ) );
        assertFalse( emp1str.contains( "customer2@gmail.com" ) );
        assertFalse( emp1str.contains( "Ms. Customer" ) );
        assertFalse( emp1str.contains( "customer3@gmail.com" ) );
        assertFalse( emp1str.contains( "Mr. Customer" ) );

        // Customer 2
        emp2str = mvc.perform( get( "/api/v1/customers/email/customer2@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp2str.contains( "customer1@gmail.com" ) );
        assertFalse( emp2str.contains( "Mrs. Customer" ) );
        assertTrue( emp2str.contains( "customer2@gmail.com" ) );
        assertTrue( emp2str.contains( "Ms. Customer" ) );
        assertFalse( emp2str.contains( "customer3@gmail.com" ) );
        assertFalse( emp2str.contains( "Mr. Customer" ) );

        // Customer 3
        emp3str = mvc.perform( get( "/api/v1/customers/email/customer3@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp3str.contains( "customer1@gmail.com" ) );
        assertFalse( emp3str.contains( "Mrs. Customer" ) );
        assertFalse( emp3str.contains( "customer2@gmail.com" ) );
        assertFalse( emp3str.contains( "Ms. Customer" ) );
        assertTrue( emp3str.contains( "customer3@gmail.com" ) );
        assertTrue( emp3str.contains( "Mr. Customer" ) );

        // Test Getting an Customer that does not exist
        mvc.perform( get( "/api/v1/customers/email/email@gmail.com" ) ).andDo( print() )
                .andExpect( status().isNotFound() );

    }

    /**
     * Tests the delete API endpoints
     */
    @Test
    @Transactional
    public void testDelete () throws Exception {
        service.deleteAll();
        // Create Customers
        final Customer cust1 = new Customer( "customer1@gmail.com", "Mrs. Customer", "cust1password" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust1 ) ) ).andExpect( status().isOk() );

        assertEquals( 1, service.count() );

        final Customer cust2 = new Customer( "customer2@gmail.com", "Ms. Customer", "cust2password" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust2 ) ) ).andExpect( status().isOk() );

        assertEquals( 2, service.count() );

        final Customer cust3 = new Customer( "customer3@gmail.com", "Mr. Customer", "cust3password" );

        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cust3 ) ) ).andExpect( status().isOk() );

        assertEquals( 3, service.count() );
        String employees = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( employees.contains( "customer1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Customer" ) );
        assertTrue( employees.contains( "customer2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Customer" ) );
        assertTrue( employees.contains( "customer3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Customer" ) );

        // Test Delete Employee 2
        mvc.perform( delete( "/api/v1/customers/" + service.findByEmail( "customer2@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 2, service.count() );
        employees = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "customer1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Customer" ) );
        assertFalse( employees.contains( "customer2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Customer" ) );
        assertTrue( employees.contains( "customer3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Customer" ) );

        // Test Delete Customer 1
        mvc.perform( delete( "/api/v1/customers/" + service.findByEmail( "customer1@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 1, service.count() );
        employees = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( employees.contains( "customer1@gmail.com" ) );
        assertFalse( employees.contains( "Mrs. Customer" ) );
        assertFalse( employees.contains( "customer2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Customer" ) );
        assertTrue( employees.contains( "customer3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Customer" ) );

        // Test Delete Customer 3
        mvc.perform( delete( "/api/v1/customers/" + service.findByEmail( "customer3@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 0, service.count() );
        employees = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( employees.contains( "customer1@gmail.com" ) );
        assertFalse( employees.contains( "Mrs. Customer" ) );
        assertFalse( employees.contains( "customer2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Customer" ) );
        assertFalse( employees.contains( "customer3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Customer" ) );

        // Try Deleting an employee that doesn't exist
        mvc.perform( delete( "/api/v1/customers/12345" ) ).andExpect( status().isNotFound() );

    }

    /**
     * Tests validating a user's email and password
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testValidateCustomer () throws Exception {
        final UserInfo customer = new UserInfo( "customer@gmail.com", "Customer", "password" );
        System.out.println( "***Pass after test construction: " + customer.getPassword() );
        mvc.perform( post( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( customer ) ) ).andExpect( status().isOk() );

        final UserInfo attempt = new UserInfo( "customer@gmail.com", null, "password" );
        mvc.perform( post( "/api/v1/customers/validate" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( attempt ) ) ).andExpect( status().isOk() );
    }

}
