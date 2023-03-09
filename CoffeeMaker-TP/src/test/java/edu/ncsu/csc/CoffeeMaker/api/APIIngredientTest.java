package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

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
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * Testing class for API Ingredient Testing to test the API functions of
 * CoffeeMaker
 *
 * @author McKenna Corn
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {

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

    /** The IngredientService object to store Ingredients in the database */
    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Creates a test Ingredient to show that the API sets up correctly and adds
     * Ingredients correctly
     *
     * @throws Exception
     *             ie
     */
    @Test
    @Transactional
    public void testIngredientAPI () throws Exception {
        Assertions.assertEquals( 0, service.count() );

        final Ingredient ing = new Ingredient( "Milk", 1 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ing ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    /**
     * Creates Ingredients and posts to the API and receives the data to check
     * with the local database
     *
     * @throws Exception
     *             ie
     */
    @Test
    @Transactional
    public void testgGetIngredient () throws Exception {
        service.deleteAll();
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = new Ingredient( "Milk", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

        final Ingredient i2 = new Ingredient( "Sugar", 3 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) );

        Assertions.assertEquals( 2, (int) service.count() );

        final String ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertTrue( ingredients.contains( "Sugar" ) );
        Assertions.assertTrue( ingredients.contains( "Milk" ) );

        // just looks at the one ingredient requested
        final String sugar = mvc.perform( get( "/api/v1/ingredients/Sugar" ) ).andDo( print() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertTrue( sugar.contains( "Sugar" ) );
        Assertions.assertFalse( sugar.contains( "Milk" ) );

        // just looks at the one ingredient requested
        final String milk = mvc.perform( get( "/api/v1/ingredients/Milk" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();

        // and the other
        Assertions.assertFalse( milk.contains( "Sugar" ) );
        Assertions.assertTrue( milk.contains( "Milk" ) );

        // ask for a specific ingredient that was not added
        // status().isNotFound()
        // expected!
        final String ing = mvc.perform( get( "/api/v1/ingredients/Syrup" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();
        Assertions.assertTrue( ing.contains( "No ingredient found with name Syrup" ) );
        Assertions.assertFalse( ing.contains( "Sugar" ) );
        Assertions.assertFalse( ing.contains( "Milk" ) );

    }

    /**
     * Creates Ingredients and posts to the API and receives the data to check
     * with the local database. Makes sure that ingredients that are not saved
     * but posted are not included in database
     *
     * @throws Exception
     *             ie
     */
    @Test
    @Transactional
    public void testAddIngredient () throws Exception {
        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = new Ingredient( "Milk", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.count(), "There should only one recipe in the CoffeeMaker" );

        final Ingredient i2 = new Ingredient( "Sugar", 3 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the CoffeeMaker" );

        final Ingredient i3 = new Ingredient( "Sugar", 567 );

        // Add duplicate should fail
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i3 ) ) ).andExpect( status().isConflict() );
        final String sugar = mvc.perform( get( "/api/v1/ingredients/Sugar" ) ).andDo( print() ).andReturn()
                .getResponse().getContentAsString();
        Assertions.assertFalse( sugar.contains( "amount\":567" ) );
        Assertions.assertTrue( sugar.contains( "amount\":3" ) );
        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the CoffeeMaker" );

    }

    /**
     * Creates and Updates ingredients
     *
     * @throws Exception
     *             ie
     */
    @Test
    @Transactional
    public void testUpdateIngredient () throws Exception {
        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = new Ingredient( "Milk", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.count(), "There should only one recipe in the CoffeeMaker" );

        i1.setAmount( 10 );

        mvc.perform( put( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        final String milk = mvc.perform( get( "/api/v1/ingredients/Milk" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();

        Assertions.assertTrue( milk.contains( "Milk" ) );
        Assertions.assertTrue( milk.contains( "10" ) );

        // Attempt to edit ingredient that doesn't exist - should not be found
        final Ingredient i2 = new Ingredient( "Sugar", 3 );

        mvc.perform( put( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().isNotFound() );

        Assertions.assertEquals( 1, service.count(), "There should be one Ingredient in the CoffeeMaker" );

    }

    /**
     * Deletes an Ingredient that was added to the database and check that it is
     * correctly removed from local and API
     *
     * @throws Exception
     *             ie
     */
    @Test
    @Transactional
    public void testDeleteIngredient () throws Exception {

        service.deleteAll();
        Assertions.assertEquals( 0, service.count(), "There should be no Recipes in the CoffeeMaker" );

        final Ingredient i1 = new Ingredient( "Milk", 5 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        final Ingredient i2 = new Ingredient( "Chocolate", 1 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().isOk() );

        final Ingredient i3 = new Ingredient( "Sugar", 7 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i3 ) ) ).andExpect( status().isOk() );

        String ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();

        Assertions.assertTrue( ingredients.contains( "Milk" ) );
        Assertions.assertTrue( ingredients.contains( "Chocolate" ) );
        Assertions.assertTrue( ingredients.contains( "Sugar" ) );

        mvc.perform( delete( "/api/v1/ingredients/Milk" ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();

        Assertions.assertFalse( ingredients.contains( "Milk" ) );
        Assertions.assertTrue( ingredients.contains( "Chocolate" ) );
        Assertions.assertTrue( ingredients.contains( "Sugar" ) );

        mvc.perform( delete( "/api/v1/ingredients/Sugar" ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();

        Assertions.assertFalse( ingredients.contains( "Milk" ) );
        Assertions.assertTrue( ingredients.contains( "Chocolate" ) );
        Assertions.assertFalse( ingredients.contains( "Sugar" ) );

        mvc.perform( delete( "/api/v1/ingredients/Chocolate" ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();

        Assertions.assertFalse( ingredients.contains( "Milk" ) );
        Assertions.assertFalse( ingredients.contains( "Chocolate" ) );
        Assertions.assertFalse( ingredients.contains( "Sugar" ) );

        Assertions.assertEquals( 0, service.count() );

        final String delete = mvc.perform( delete( "/api/v1/ingredients/Coffee" ) ).andExpect( status().isNotFound() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( delete.contains( "No ingredient found for name Coffee" ) );
    }

}
