package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Testing class for API Recipe Testing to test the API functions of CoffeeMaker
 *
 * @author McKenna Corn
 * @author Gabriel Watts
 * @author Erin Grouge
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

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
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Tests creating and posting a recipe
     *
     * @throws Exception
     *             if there is an error
     *
     */
    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe( "Mocha", 10 );
        r.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r.addIngredient( new Ingredient( "Coffee", 3 ) );
        r.addIngredient( new Ingredient( "Milk", 4 ) );
        r.addIngredient( new Ingredient( "Sugar", 8 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );
        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( recipe.contains( "Mocha" ) );

    }

    /**
     * Tests getting recipes
     *
     * @throws Exception
     *             ie
     */
    @Test
    @Transactional
    public void testGetRecipe () throws Exception {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) );

        final Recipe r2 = new Recipe( "Mocha", 50 );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) );

        final String recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( recipes.contains( "Coffee" ) );
        Assertions.assertTrue( recipes.contains( "Mocha" ) );

        // just looks at the one recipe requested
        final String recipe = mvc.perform( get( "/api/v1/recipes/Coffee" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( recipe.contains( "Coffee" ) );
        Assertions.assertFalse( recipe.contains( "Mocha" ) );

        // ask for a specific recipe that was not added status().isNotFound()
        // expected!
        final String recipe2 = mvc.perform( get( "/api/v1/recipes/Latte" ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( recipe2.contains( "No recipe found with name" ) );
        Assertions.assertFalse( recipe2.contains( "Coffee" ) );
        Assertions.assertFalse( recipe2.contains( "Mocha" ) );

    }

    /**
     * Tests adding a duplicate recipe
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testAddingDuplicate () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe( name, 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) );

        final Recipe r2 = new Recipe( name, 50 );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    /**
     * Tests Adding more the 3 (the maximum) recipes
     *
     * @throws Exception
     *             if theres an error
     */
    @Test
    @Transactional
    public void testAddMoreThan3Recipes () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) );

        final Recipe r2 = new Recipe( "Mocha", 50 );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) );

        final Recipe r3 = new Recipe( "Latte", 60 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 2 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = new Recipe( "Hot Chocolate", 75 );
        r4.addIngredient( new Ingredient( "Milk", 2 ) );
        r4.addIngredient( new Ingredient( "Sugar", 1 ) );
        r4.addIngredient( new Ingredient( "Chocolate", 2 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    /**
     * Tests editing a recipe
     */
    @Test
    @Transactional
    public void testEditRecipe () throws Exception {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        String coffee = mvc.perform( get( "/api/v1/recipes/Coffee" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( coffee.contains( "\"name\":\"Coffee\"" ) ); // Check
                                                                           // info
                                                                           // before
                                                                           // changes.
        Assertions.assertTrue( coffee.contains( "\"price\":50" ) );
        Assertions.assertTrue( coffee.contains( "\"name\":\"Coffee\",\"amount\":3" ) );
        Assertions.assertTrue( coffee.contains( "\"name\":\"Milk\",\"amount\":1" ) );
        Assertions.assertTrue( coffee.contains( "\"name\":\"Sugar\",\"amount\":1" ) );

        final Recipe r2 = new Recipe( "Double Coffee", 100 );
        r2.addIngredient( new Ingredient( "Coffee", 6 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );

        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );

        coffee = mvc.perform( get( "/api/v1/recipes/Coffee" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( coffee.contains( "\"name\":\"Coffee\"" ) ); // same
                                                                           // name
        Assertions.assertTrue( coffee.contains( "\"price\":100" ) ); // updated
                                                                     // price
        Assertions.assertTrue( coffee.contains( "\"name\":\"Coffee\",\"amount\":6" ) ); // updated
                                                                                        // ingredients
        Assertions.assertTrue( coffee.contains( "\"name\":\"Milk\",\"amount\":2" ) );
        Assertions.assertTrue( coffee.contains( "\"name\":\"Sugar\",\"amount\":2" ) );

    }

    /**
     * Testing deleting recipes
     *
     * @throws Exception
     *             if there's an error.
     *
     */
    @Test
    @Transactional
    public void testDeleteRecipe () throws Exception {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        final Recipe r2 = new Recipe( "Mocha", 50 );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );

        final Recipe r3 = new Recipe( "Latte", 60 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 2 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isOk() );

        String recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( recipes.contains( "Coffee" ) );
        Assertions.assertTrue( recipes.contains( "Mocha" ) );
        Assertions.assertTrue( recipes.contains( "Latte" ) );

        mvc.perform( delete( "/api/v1/recipes/Mocha" ) ).andExpect( status().isOk() );

        recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertTrue( recipes.contains( "Coffee" ) );
        Assertions.assertFalse( recipes.contains( "Mocha" ) );
        Assertions.assertTrue( recipes.contains( "Latte" ) );

        mvc.perform( delete( "/api/v1/recipes/Latte" ) ).andExpect( status().isOk() );

        recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertTrue( recipes.contains( "Coffee" ) );
        Assertions.assertFalse( recipes.contains( "Mocha" ) );
        Assertions.assertFalse( recipes.contains( "Latte" ) );

        mvc.perform( delete( "/api/v1/recipes/Coffee" ) ).andExpect( status().isOk() );

        recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertFalse( recipes.contains( "Coffee" ) );
        Assertions.assertFalse( recipes.contains( "Mocha" ) );
        Assertions.assertFalse( recipes.contains( "Latte" ) );

        // add a special test case for when delete is null
        mvc.perform( delete( "/api/v1/recipes/Coffee" ) ).andExpect( status().isNotFound() ).andReturn().getResponse()
                .getContentAsString();

    }

    /**
     * Tests getting arbitrary ingredients
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testGetIngredients () throws Exception {
        Assertions.assertEquals( 0, service.count(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        final String ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":3" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Milk\",\"amount\":1" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Sugar\",\"amount\":1" ) );

        final String coffee = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients/Coffee" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( coffee.contains( "\"name\":\"Coffee\",\"amount\":3" ) );

        final String milk = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients/Milk" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( milk.contains( "\"name\":\"Milk\",\"amount\":1" ) );

        final String sugar = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients/Sugar" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( sugar.contains( "\"name\":\"Sugar\",\"amount\":1" ) );

        // Get ingredient that doesn't exist
        final String syrup = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients/Syrup" ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( syrup.contains( "No ingredient found with name Syrup" ) );

        // Try when no ingredients
        final Recipe r2 = new Recipe( "Chai", 70 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );

        final String noIngredients = mvc.perform( get( "/api/v1/recipes/Chai/ingredients" ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( noIngredients.contains( "Recipe found with name Chai has no ingredients" ) );
    }

    /**
     * Test updating an ingredient in a recipe
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testUpdateIngredient () throws Exception {
        Assertions.assertEquals( 0, service.count(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        String ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":3" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Milk\",\"amount\":1" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Sugar\",\"amount\":1" ) );

        final Ingredient i = new Ingredient( "Milk", 2 );

        mvc.perform( put( "/api/v1/recipes/Coffee/ingredients/Milk" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":3" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Milk\",\"amount\":2" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Sugar\",\"amount\":1" ) );

    }

    /**
     * Tests Adding Ingredients through API Endpoints
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testAddIngredient () throws Exception {
        Assertions.assertEquals( 0, service.count(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 50 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        String ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( ingredients.contains( "Recipe found with name Coffee has no ingredients" ) );

        final Ingredient i1 = new Ingredient( "Coffee", 4 );
        final Ingredient i2 = new Ingredient( "Milk", 3 );
        final Ingredient i3 = new Ingredient( "Sugar", 2 );

        mvc.perform( post( "/api/v1/recipes/Coffee/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":4" ) );
        Assertions.assertFalse( ingredients.contains( "\"name\":\"Milk\",\"amount\":3" ) );
        Assertions.assertFalse( ingredients.contains( "\"name\":\"Sugar\",\"amount\":2" ) );

        mvc.perform( post( "/api/v1/recipes/Coffee/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":4" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Milk\",\"amount\":3" ) );
        Assertions.assertFalse( ingredients.contains( "\"name\":\"Sugar\",\"amount\":2" ) );

        mvc.perform( post( "/api/v1/recipes/Coffee/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i3 ) ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":4" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Milk\",\"amount\":3" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Sugar\",\"amount\":2" ) );

    }

    /**
     * Test deleting ingredients from recipe
     *
     * @throws Exception
     *             if there's an error
     */
    @Test
    @Transactional
    public void testDeleteIngredients () throws Exception {
        Assertions.assertEquals( 0, service.count(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 4 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Sugar", 2 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        String ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":4" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Milk\",\"amount\":3" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Sugar\",\"amount\":2" ) );

        mvc.perform( delete( "/api/v1/recipes/Coffee/ingredients/Milk" ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":4" ) );
        Assertions.assertFalse( ingredients.contains( "\"name\":\"Milk\",\"amount\":3" ) );
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Sugar\",\"amount\":2" ) );

        mvc.perform( delete( "/api/v1/recipes/Coffee/ingredients/Sugar" ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "\"name\":\"Coffee\",\"amount\":4" ) );
        Assertions.assertFalse( ingredients.contains( "\"name\":\"Milk\",\"amount\":3" ) );
        Assertions.assertFalse( ingredients.contains( "\"name\":\"Sugar\",\"amount\":2" ) );

        mvc.perform( delete( "/api/v1/recipes/Coffee/ingredients/Coffee" ) ).andExpect( status().isOk() );

        ingredients = mvc.perform( get( "/api/v1/recipes/Coffee/ingredients" ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredients.contains( "Recipe found with name Coffee has no ingredients" ) );

        // Invalid path
        // Delete Ingredient that does not exist
        final String msg = mvc.perform( delete( "/api/v1/recipes/Coffee/ingredients/Syrup" ) )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( msg.contains( "No ingredient found for name Syrup in Coffee" ) );
    }
}
