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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the API functionality by creating a recipe, adding inventory, making
 * coffee and checking that they were added and made successfully.
 *
 * @author Erin Grouge
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

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
    private RecipeService         service;
    
    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();

    }

    /**
     * Tests API functionality by adding a recipe and inventory, and then making
     * coffee
     *
     * @throws Exception
     *             if there are errors
     */
    @Test
    @Transactional
    public void testAPI () throws Exception {

        /* Get recipes */
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /*
         * Check to see if Mocha recipe already exists. If not, make and post
         * one
         */
        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe rec = new Recipe( "Mocha", 10 );
            rec.addIngredient( new Ingredient( "Coffee", 4 ) );
            rec.addIngredient( new Ingredient( "Milk", 4 ) );
            rec.addIngredient( new Ingredient( "Sugar", 4 ) );
            rec.addIngredient( new Ingredient( "Chocolate", 4 ) );


            /* Post new recipe */
            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( rec ) ) ).andExpect( status().isOk() );
        }

        /* Check to make sure new Mocha recipe is there */
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertTrue( recipe.contains( "Mocha" ) );

        /* Add inventory so the mocha can be made */
        final Inventory in = new Inventory();
        in.addIngredient( new Ingredient( "Coffee", 50 ) );
        in.addIngredient( new Ingredient( "Milk", 50 ) );
        in.addIngredient( new Ingredient( "Sugar", 50 ) );
        in.addIngredient( new Ingredient( "Chocolate", 50 ) );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( in ) ) ).andExpect( status().isOk() );

        // Store Inventory Values before making coffee to check inventory after
        final String inventoryBefore = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( inventoryBefore.contains( "50" ) );

        /* Make Coffee */
        mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 20 ) ) ).andExpect( status().isOk() );

        // Retrieve inventory after making the coffee to check if it was updated
        // correctly
        final String inventoryAfter = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( inventoryAfter.contains( "46" ) );

    }

}
