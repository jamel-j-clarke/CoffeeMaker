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
import edu.ncsu.csc.CoffeeMaker.models.users.Employee;
import edu.ncsu.csc.CoffeeMaker.services.EmployeeService;

/**
 * Tests the APIEmployeeController class and its methods.
 *
 * @author Erin Grouge
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIEmployeeControllerTest {

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
    private EmployeeService       service;

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
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testPost () throws Exception {
        // Post Employee 1
        final Employee erin = new Employee( "erin@gmail.com", "Erin Grouge", "erin'spassword" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( erin ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 1, service.count() );

        // Retrieve the contents with a get request
        String emp = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( emp.contains( "Erin Grouge" ) );
        assertTrue( emp.contains( "erin@gmail.com" ) );
        assertTrue( emp.contains( "erin'spassword" ) );

        // Post Employee 2
        final Employee emma = new Employee( "emma@gmail.com", "Emma Holincheck", "emma'spassword" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emma ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 2, service.count() );

        // Retrieve the contents with a get request
        emp = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( emp.contains( "Emma Holincheck" ) );
        assertTrue( emp.contains( "emma@gmail.com" ) );
        assertTrue( emp.contains( "emma'spassword" ) );

        // Post Employee 3
        final Employee jonathan = new Employee( "jonathan@gmail.com", "Jonathan Kurian", "jonathan'spassword" );
        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( jonathan ) ) ).andExpect( status().isOk() );

        // Check that it is saved in service
        assertEquals( 3, service.count() );

        // Retrieve the contents with a get request
        emp = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( emp.contains( "Jonathan Kurian" ) );
        assertTrue( emp.contains( "jonathan@gmail.com" ) );
        assertTrue( emp.contains( "jonathan'spassword" ) );

        // Check that employee with duplicate email will not be created.
        final Employee jonathanDupe = new Employee( "jonathan@gmail.com", "Jon Kurian", "jon'spassword" );
        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( jonathanDupe ) ) ).andExpect( status().isConflict() );

        // Check that there is still only 3
        assertEquals( 3, service.count() );
        emp = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( emp.contains( "Jonathan Kurian" ) );
        assertTrue( emp.contains( "jonathan@gmail.com" ) );
        assertTrue( emp.contains( "jonathan'spassword" ) );
        assertFalse( emp.contains( "Jon Kurian" ) );
        assertFalse( emp.contains( "jon'spassword" ) );

    }

    // /**
    // * Tests the put API endpoints with email
    // *
    // * @throws Exception
    // * if there is an error
    // */
    // @Test
    // @Transactional
    // public void testPutEmail () throws Exception {
    // service.deleteAll();
    //
    // // Create an Employee
    // final Employee emp = new Employee( "employee@gmail.com", "Mrs. Barista",
    // "emppassword" );
    //
    // mvc.perform( post( "/api/v1/employees" ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( emp ) ) ).andExpect( status().isOk() );
    //
    // String employees = mvc.perform( get( "/api/v1/employees" ) ).andDo(
    // print() ).andExpect( status().isOk() )
    // .andReturn().getResponse().getContentAsString();
    // assertTrue( employees.contains( "employee@gmail.com" ) );
    // assertTrue( employees.contains( "Mrs. Barista" ) );
    // assertTrue( employees.contains( "emppassword" ) );
    // assertEquals( 1, service.count() );
    //
    // final Long id = (Long) service.findByEmail( "employee@gmail.com"
    // ).getId();
    //
    // // Update the employee - change name
    // final Employee empUpdateName = new Employee( "employee@gmail.com", "Ms.
    // Barista", "emppassword" );
    //
    // mvc.perform( put( "/api/v1/employees/" + emp.getEmail() ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( empUpdateName ) ) ).andExpect(
    // status().isOk() );
    //
    // employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print()
    // ).andExpect( status().isOk() ).andReturn()
    // .getResponse().getContentAsString();
    // assertTrue( employees.contains( "employee@gmail.com" ) );
    // assertTrue( employees.contains( "Ms. Barista" ) );
    // assertFalse( employees.contains( "Mrs. Barista" ) );
    // assertTrue( employees.contains( "emppassword" ) );
    // assertTrue( employees.contains( "" + id ) );
    // assertEquals( 1, service.count() );
    //
    // // Update the employee - change password
    // final Employee empUpdatePassword = new Employee( "employee@gmail.com",
    // "Ms. Barista", "empnewpassword" );
    //
    // mvc.perform( put( "/api/v1/employees/" + emp.getEmail() ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( empUpdatePassword ) ) ).andExpect(
    // status().isOk() );
    //
    // employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print()
    // ).andExpect( status().isOk() ).andReturn()
    // .getResponse().getContentAsString();
    // assertTrue( employees.contains( "employee@gmail.com" ) );
    // assertTrue( employees.contains( "Ms. Barista" ) );
    // assertTrue( employees.contains( "empnewpassword" ) );
    // assertFalse( employees.contains( "emppassword" ) );
    // assertTrue( employees.contains( "" + id ) );
    // assertEquals( 1, service.count() );
    //
    // // Update the employee - change email (Not allowed)
    // final Employee empUpdateEmail = new Employee( "emp@gmail.com", "Ms.
    // Barista", "empnewpassword" );
    //
    // mvc.perform( put( "/api/v1/employees/" + emp.getEmail() ).contentType(
    // MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( empUpdateEmail ) ) ).andExpect(
    // status().isOk() );
    //
    // employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print()
    // ).andExpect( status().isOk() ).andReturn()
    // .getResponse().getContentAsString();
    // assertTrue( employees.contains( "employee@gmail.com" ) );
    // assertTrue( employees.contains( "Ms. Barista" ) );
    // assertTrue( employees.contains( "empnewpassword" ) );
    // assertFalse( employees.contains( "emp@gmail.com" ) ); // Cannot change
    // // email
    // assertTrue( employees.contains( "" + id ) );
    // assertEquals( 1, service.count() );
    // }

    /**
     * Tests the put API endpoints with ID - commented out until email vs. id is
     * chosen
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testPut () throws Exception {
        service.deleteAll();
        // Create an Employee final
        final Employee emp = new Employee( "employee@gmail.com", "Mrs. Barista", "emppassword" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emp ) ) ).andExpect( status().isOk() );

        String employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( employees.contains( "employee@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Barista" ) );
        assertTrue( employees.contains( "emppassword" ) );
        assertEquals( 1, service.count() );

        final Long id = (Long) service.findByEmail( "employee@gmail.com" ).getId();

        // Update the employee - change name final
        final Employee empUpdateName = new Employee( "employee@gmail.com", "Ms. Barista", "emppassword" );

        mvc.perform( put( "/api/v1/employees/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( empUpdateName ) ) ).andExpect( status().isOk() );

        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "employee@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Barista" ) );
        assertFalse( employees.contains( "Mrs. Barista" ) );
        assertTrue( employees.contains( "emppassword" ) );
        assertTrue( employees.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Update the employee - change password final
        final Employee empUpdatePassword = new Employee( "employee@gmail.com", "Ms. Barista", "empnewpassword" );

        mvc.perform( put( "/api/v1/employees/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( empUpdatePassword ) ) ).andExpect( status().isOk() );
        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "employee@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Barista" ) );
        assertTrue( employees.contains( "empnewpassword" ) );
        assertFalse( employees.contains( "emppassword" ) );
        assertTrue( employees.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Update the employee - change email (Not allowed) final
        final Employee empUpdateEmail = new Employee( "emp@gmail.com", "Ms. Barista", "empnewpassword" );

        mvc.perform( put( "/api/v1/employees/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( empUpdateEmail ) ) ).andExpect( status().isOk() );

        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "employee@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Barista" ) );
        assertTrue( employees.contains( "empnewpassword" ) );
        assertFalse( employees.contains( "emp@gmail.com" ) ); // Cannot change
                                                              // email
        assertTrue( employees.contains( "" + id ) );
        assertEquals( 1, service.count() );

        // Test putting with an id that does not exist
        mvc.perform( put( "/api/v1/employees/12345" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( empUpdateEmail ) ) ).andExpect( status().isNotFound() );

    }

    /**
     * Tests the get API endpoints
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testGet () throws Exception {
        service.deleteAll();
        // Create Employees
        final Employee emp1 = new Employee( "employee1@gmail.com", "Mrs. Barista", "emp1password" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emp1 ) ) ).andExpect( status().isOk() );

        assertEquals( 1, service.count() );
        String employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( employees.contains( "employee1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Barista" ) );
        assertTrue( employees.contains( "emp1password" ) );
        assertFalse( employees.contains( "employee2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Barista" ) );
        assertFalse( employees.contains( "emp2password" ) );
        assertFalse( employees.contains( "employee3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Barista" ) );
        assertFalse( employees.contains( "emp3password" ) );

        final Employee emp2 = new Employee( "employee2@gmail.com", "Ms. Barista", "emp2password" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emp2 ) ) ).andExpect( status().isOk() );

        assertEquals( 2, service.count() );
        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "employee1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Barista" ) );
        assertTrue( employees.contains( "emp1password" ) );
        assertTrue( employees.contains( "employee2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Barista" ) );
        assertTrue( employees.contains( "emp2password" ) );
        assertFalse( employees.contains( "employee3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Barista" ) );
        assertFalse( employees.contains( "emp3password" ) );

        final Employee emp3 = new Employee( "employee3@gmail.com", "Mr. Barista", "emp3password" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emp3 ) ) ).andExpect( status().isOk() );

        assertEquals( 3, service.count() );
        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "employee1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Barista" ) );
        assertTrue( employees.contains( "emp1password" ) );
        assertTrue( employees.contains( "employee2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Barista" ) );
        assertTrue( employees.contains( "emp2password" ) );
        assertTrue( employees.contains( "employee3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Barista" ) );
        assertTrue( employees.contains( "emp3password" ) );

        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Get Employees by ID
        // Employee 1
        String emp1str = mvc
                .perform( get( "/api/v1/employees/" + service.findByEmail( "employee1@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertTrue( emp1str.contains( "employee1@gmail.com" ) );
        assertTrue( emp1str.contains( "Mrs. Barista" ) );
        assertTrue( emp1str.contains( "emp1password" ) );
        assertTrue( emp1str.contains( "" + service.findByEmail( "employee1@gmail.com" ).getId() ) );
        assertFalse( emp1str.contains( "employee2@gmail.com" ) );
        assertFalse( emp1str.contains( "Ms. Barista" ) );
        assertFalse( emp1str.contains( "emp2password" ) );
        assertFalse( emp1str.contains( "employee3@gmail.com" ) );
        assertFalse( emp1str.contains( "Mr. Barista" ) );
        assertFalse( emp1str.contains( "emp3password" ) );

        // Employee 2
        String emp2str = mvc
                .perform( get( "/api/v1/employees/" + service.findByEmail( "employee2@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp2str.contains( "employee1@gmail.com" ) );
        assertFalse( emp2str.contains( "Mrs. Barista" ) );
        assertFalse( emp2str.contains( "emp1password" ) );
        assertTrue( emp2str.contains( "employee2@gmail.com" ) );
        assertTrue( emp2str.contains( "Ms. Barista" ) );
        assertTrue( emp2str.contains( "emp2password" ) );
        assertTrue( emp2str.contains( "" + service.findByEmail( "employee2@gmail.com" ).getId() ) );
        assertFalse( emp2str.contains( "employee3@gmail.com" ) );
        assertFalse( emp2str.contains( "Mr. Barista" ) );
        assertFalse( emp2str.contains( "emp3password" ) );

        // Employee 3
        String emp3str = mvc
                .perform( get( "/api/v1/employees/" + service.findByEmail( "employee3@gmail.com" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp3str.contains( "employee1@gmail.com" ) );
        assertFalse( emp3str.contains( "Mrs. Barista" ) );
        assertFalse( emp3str.contains( "emp1password" ) );
        assertFalse( emp3str.contains( "employee2@gmail.com" ) );
        assertFalse( emp3str.contains( "Ms. Barista" ) );
        assertFalse( emp3str.contains( "emp2password" ) );
        assertTrue( emp3str.contains( "employee3@gmail.com" ) );
        assertTrue( emp3str.contains( "Mr. Barista" ) );
        assertTrue( emp3str.contains( "emp3password" ) );
        assertTrue( emp3str.contains( "" + service.findByEmail( "employee3@gmail.com" ).getId() ) );

        // Test Getting an employee that does not exist
        mvc.perform( get( "/api/v1/employees/12345" ) ).andDo( print() ).andExpect( status().isNotFound() );

        // Get Employee by Email
        // Employee 1
        emp1str = mvc.perform( get( "/api/v1/employees/email/employee1@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertTrue( emp1str.contains( "employee1@gmail.com" ) );
        assertTrue( emp1str.contains( "Mrs. Barista" ) );
        assertTrue( emp1str.contains( "emp1password" ) );
        assertTrue( emp1str.contains( "" + service.findByEmail( "employee1@gmail.com" ).getId() ) );
        assertFalse( emp1str.contains( "employee2@gmail.com" ) );
        assertFalse( emp1str.contains( "Ms. Barista" ) );
        assertFalse( emp1str.contains( "emp2password" ) );
        assertFalse( emp1str.contains( "employee3@gmail.com" ) );
        assertFalse( emp1str.contains( "Mr. Barista" ) );
        assertFalse( emp1str.contains( "emp3password" ) );

        // Employee 2
        emp2str = mvc.perform( get( "/api/v1/employees/email/employee2@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp2str.contains( "employee1@gmail.com" ) );
        assertFalse( emp2str.contains( "Mrs. Barista" ) );
        assertFalse( emp2str.contains( "emp1password" ) );
        assertTrue( emp2str.contains( "employee2@gmail.com" ) );
        assertTrue( emp2str.contains( "Ms. Barista" ) );
        assertTrue( emp2str.contains( "emp2password" ) );
        assertTrue( emp2str.contains( "" + service.findByEmail( "employee2@gmail.com" ).getId() ) );
        assertFalse( emp2str.contains( "employee3@gmail.com" ) );
        assertFalse( emp2str.contains( "Mr. Barista" ) );
        assertFalse( emp2str.contains( "emp3password" ) );

        // Employee 3
        emp3str = mvc.perform( get( "/api/v1/employees/email/employee3@gmail.com" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( 3, service.count() );
        assertFalse( emp3str.contains( "employee1@gmail.com" ) );
        assertFalse( emp3str.contains( "Mrs. Barista" ) );
        assertFalse( emp3str.contains( "emp1password" ) );
        assertFalse( emp3str.contains( "employee2@gmail.com" ) );
        assertFalse( emp3str.contains( "Ms. Barista" ) );
        assertFalse( emp3str.contains( "emp2password" ) );
        assertTrue( emp3str.contains( "employee3@gmail.com" ) );
        assertTrue( emp3str.contains( "Mr. Barista" ) );
        assertTrue( emp3str.contains( "emp3password" ) );
        assertTrue( emp3str.contains( "" + service.findByEmail( "employee3@gmail.com" ).getId() ) );

        // Test Getting an employee that does not exist
        mvc.perform( get( "/api/v1/employees/email/email@gmail.com" ) ).andDo( print() )
                .andExpect( status().isNotFound() );

    }

    /**
     * Tests the delete API endpoints
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testDelete () throws Exception {
        service.deleteAll();
        // Create Employees
        final Employee emp1 = new Employee( "employee1@gmail.com", "Mrs. Barista", "emp1password" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emp1 ) ) ).andExpect( status().isOk() );

        assertEquals( 1, service.count() );

        final Employee emp2 = new Employee( "employee2@gmail.com", "Ms. Barista", "emp2password" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emp2 ) ) ).andExpect( status().isOk() );

        assertEquals( 2, service.count() );

        final Employee emp3 = new Employee( "employee3@gmail.com", "Mr. Barista", "emp3password" );

        mvc.perform( post( "/api/v1/employees" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( emp3 ) ) ).andExpect( status().isOk() );

        assertEquals( 3, service.count() );
        String employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( employees.contains( "employee1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Barista" ) );
        assertTrue( employees.contains( "emp1password" ) );
        assertTrue( employees.contains( "employee2@gmail.com" ) );
        assertTrue( employees.contains( "Ms. Barista" ) );
        assertTrue( employees.contains( "emp2password" ) );
        assertTrue( employees.contains( "employee3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Barista" ) );
        assertTrue( employees.contains( "emp3password" ) );

        // Test Delete Employee 2
        mvc.perform( delete( "/api/v1/employees/" + service.findByEmail( "employee2@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 2, service.count() );
        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( employees.contains( "employee1@gmail.com" ) );
        assertTrue( employees.contains( "Mrs. Barista" ) );
        assertTrue( employees.contains( "emp1password" ) );
        assertFalse( employees.contains( "employee2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Barista" ) );
        assertFalse( employees.contains( "emp2password" ) );
        assertTrue( employees.contains( "employee3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Barista" ) );
        assertTrue( employees.contains( "emp3password" ) );

        // Test Delete Employee 1
        mvc.perform( delete( "/api/v1/employees/" + service.findByEmail( "employee1@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 1, service.count() );
        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( employees.contains( "employee1@gmail.com" ) );
        assertFalse( employees.contains( "Mrs. Barista" ) );
        assertFalse( employees.contains( "emp1password" ) );
        assertFalse( employees.contains( "employee2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Barista" ) );
        assertFalse( employees.contains( "emp2password" ) );
        assertTrue( employees.contains( "employee3@gmail.com" ) );
        assertTrue( employees.contains( "Mr. Barista" ) );
        assertTrue( employees.contains( "emp3password" ) );

        // Test Delete Employee 3
        mvc.perform( delete( "/api/v1/employees/" + service.findByEmail( "employee3@gmail.com" ).getId() ) )
                .andExpect( status().isOk() );
        assertEquals( 0, service.count() );
        employees = mvc.perform( get( "/api/v1/employees" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( employees.contains( "employee1@gmail.com" ) );
        assertFalse( employees.contains( "Mrs. Barista" ) );
        assertFalse( employees.contains( "emp1password" ) );
        assertFalse( employees.contains( "employee2@gmail.com" ) );
        assertFalse( employees.contains( "Ms. Barista" ) );
        assertFalse( employees.contains( "emp2password" ) );
        assertFalse( employees.contains( "employee3@gmail.com" ) );
        assertFalse( employees.contains( "Mr. Barista" ) );
        assertFalse( employees.contains( "emp3password" ) );

        // Try Deleting an employee that doesn't exist
        mvc.perform( delete( "/api/v1/employees/12345" ) ).andExpect( status().isNotFound() );

    }
}
