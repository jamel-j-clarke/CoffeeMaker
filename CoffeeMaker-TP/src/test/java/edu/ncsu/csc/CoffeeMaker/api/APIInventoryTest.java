package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Tests the API Inventory functionality by adding to inventory and retreiving
 * it.
 *
 * @author Erin Grouge
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIInventoryTest {
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
    private InventoryService      service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();

    }

    /**
     * Test APIInventory methods
     *
     * @throws Exception
     *             if there is an error
     *
     */
    @Test
    @Transactional
    public void testInventoryAPI () throws Exception {
        final Inventory in = new Inventory();
        in.addIngredient( new Ingredient( "Coffee", 50 ) );
        in.addIngredient( new Ingredient( "Milk", 50 ) );
        in.addIngredient( new Ingredient( "Sugar", 50 ) );
        in.addIngredient( new Ingredient( "Chocolate", 50 ) );

        // Add inventory
        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( in ) ) ).andExpect( status().isOk() );

        String inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( inventory.contains( "\"name\":\"Coffee\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Milk\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Sugar\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Chocolate\",\"amount\":50" ) );
        System.out.println( "****" + inventory );
        // Add Individual Ingredient
        final Ingredient i = new Ingredient( "Creamer", 30 );
        mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        // Check Inventory
        inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        Assertions.assertTrue( inventory.contains( "\"name\":\"Coffee\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Milk\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Sugar\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Chocolate\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Creamer\",\"amount\":30" ) );

        // Try adding a duplicate
        mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isConflict() );
        // Make sure inventory unchanged
        inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        Assertions.assertTrue( inventory.contains( "\"name\":\"Coffee\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Milk\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Sugar\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Chocolate\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Creamer\",\"amount\":30" ) );

        // Update amount
        i.setAmount( 10 );
        mvc.perform( put( "/api/v1/inventory/Creamer" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );
        // Check to make sure changes
        inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        Assertions.assertTrue( inventory.contains( "\"name\":\"Coffee\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Milk\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Sugar\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Chocolate\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Creamer\",\"amount\":40" ) );

        // Try editing an ingredient that doesn't exist
        mvc.perform( put( "/api/v1/inventory/Caramel" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isNotFound() );

        // Make sure inventory unchanged
        inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        Assertions.assertTrue( inventory.contains( "\"name\":\"Coffee\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Milk\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Sugar\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Chocolate\",\"amount\":50" ) );
        Assertions.assertTrue( inventory.contains( "\"name\":\"Creamer\",\"amount\":40" ) );
    }
}
