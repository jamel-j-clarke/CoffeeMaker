package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/** Spring boot for web interaction */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

/*
 * Testing class for RecipeService, the database that holds our Recipe objects.
 * @author Gabriel Watts
 */
public class TestDatabaseInteraction {

    /** Instantiate a RecipeService that holds the database */
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class. Tests adding and getting a recipe.
     */
    @Test
    @Transactional
    public void testRecipes () {

        // TESTING ADDING A RECIPE
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r.setPrice( 350 );
        recipeService.save( r ); // saves 'r' to database

        // test if dbRecipes has 'r' properly in database
        final List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );
        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getIngredients().get( 0 ), dbRecipe.getIngredients().get( 0 ) );
        assertEquals( r.getIngredients().get( 1 ), dbRecipe.getIngredients().get( 1 ) );
        assertEquals( r.getIngredients().get( 2 ), dbRecipe.getIngredients().get( 2 ) );
        assertEquals( r.getIngredients().get( 3 ), dbRecipe.getIngredients().get( 3 ) );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );
        assertEquals( 1, dbRecipes.size() );

        // GETTING RECIPE BY NAME
        assertEquals( r, recipeService.findByName( "Mocha" ) );
        assertEquals( 1, dbRecipes.size() );
        // null if none in database
        assertEquals( null, recipeService.findByName( "White Mocha" ) );

        // EDITING EXISTING RECIPE
        final Recipe dbRecipeByName = recipeService.findByName( "Mocha" );
        dbRecipe.setPrice( 15 );
        dbRecipe.updateIngredient( "Sugar", new Ingredient( "Sugar", 12 ) );
        recipeService.save( dbRecipeByName );

        // VERIFY EDITED EXISTING RECIPE -
        assertEquals( 1, recipeService.count() );
        assertEquals( 15, (int) recipeService.findAll().get( 0 ).getPrice() );
        assertEquals( 12, (int) recipeService.findAll().get( 0 ).getIngredients().get( 1 ).getAmount() );
        assertEquals( "Mocha", recipeService.findAll().get( 0 ).getName() );
        assertEquals( 2, (int) recipeService.findAll().get( 0 ).getIngredients().get( 0 ).getAmount() );
        assertEquals( 1, (int) recipeService.findAll().get( 0 ).getIngredients().get( 2 ).getAmount() );
        assertEquals( 1, (int) recipeService.findAll().get( 0 ).getIngredients().get( 3 ).getAmount() );

    }

}
