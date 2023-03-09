package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the API functionality for making coffee
 *
 * @author Erin Grouge
 * @author McKenna Corn
 * @author Gabe Watts
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    @Autowired
    private MockMvc          mvc;

    /**
     * Stores and saves the recipes
     */
    @Autowired
    private RecipeService    service;

    /**
     * Stores and saves the inventory
     */
    @Autowired
    private InventoryService iService;

    /**
     * Sets up the tests with inventory and coffee recipe
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
        iService.deleteAll();

        final Inventory ivt = iService.getInventory();

        ivt.addIngredient( new Ingredient( "Chocolate", 15 ) );
        ivt.addIngredient( new Ingredient( "Milk", 15 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 15 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 15 ) );

        iService.save( ivt );

        final Recipe recipe = new Recipe( "Coffee", 50 );
        recipe.addIngredient( new Ingredient( "Coffee", 5 ) );
        recipe.addIngredient( new Ingredient( "Milk", 1 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 1 ) );

        service.save( recipe );
    }

    /**
     * Tests purchasing Coffee
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testPurchaseBeverage () throws Exception {

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 60 ) ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( 10 ) );

    }

    /**
     * Tests purchasing a beverage without enough money
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testPurchaseBeverageInsufficientPay () throws Exception {
        /* Insufficient amount paid */

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 40 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough money paid" ) );

    }

    /**
     * Tests order beverage without enough inventory to make it
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    public void testPurchaseBeverageInsufficientInventory () throws Exception {
        /* Insufficient inventory */

        final Inventory ivt = iService.getInventory();
        final Recipe r = new Recipe( "Mega Coffee", 4 );
        r.addIngredient( new Ingredient( "Coffee", 12 ) );

        ivt.useIngredients( r );
        iService.save( ivt );

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );

    }

}
