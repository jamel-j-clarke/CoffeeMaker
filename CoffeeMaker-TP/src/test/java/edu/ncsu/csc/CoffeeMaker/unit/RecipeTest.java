package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the Recipe Object Class
 *
 * @author McKenna Corn
 * @author Gabe Watts
 * @author Erin Grouge
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    /** The service object used to store the recipes */
    @Autowired
    private RecipeService service;

    /**
     * Deletes all previously stored recipes before each test
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests Adding recipes
     */
    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 5 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 5 ) );
        r2.addIngredient( new Ingredient( "Milk", 3 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
        Assertions.assertEquals( r2, recipes.get( 1 ), "The retrieved recipe should match the created one" );
        Assertions.assertNotNull( service.findByName( "Black Coffee" ) );
        Assertions.assertNotNull( service.findByName( "Mocha" ) );
    }

    /**
     * Testing saveAll() method
     */
    @Test
    @Transactional
    public void testSaveAllRecipes () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 5 ) );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 5 ) );
        r2.addIngredient( new Ingredient( "Milk", 3 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );

        final List<Recipe> recipes = List.of( r1, r2 );

        service.saveAll( recipes );

    }

    /**
     * Test saving three recipes (the max)
     */
    @Test
    @Transactional
    public void testAddThreeRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(),
                "Creating one recipe should result in one recipe in the database" );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 5 ) );
        r2.addIngredient( new Ingredient( "Milk", 3 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

        final Recipe r3 = new Recipe();
        r3.setName( "Mocha" );
        r3.setPrice( 1 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 2 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    /**
     * Testing Deleting a recipe
     */
    @Test
    @Transactional
    public void testDeleteRecipe () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );
        Assertions.assertEquals( "Coffee", service.findByName( "Coffee" ).getName(), "The name is correct." );
        Assertions.assertEquals( null, service.findByName( "Cafe" ), "The name DNE." );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        Assertions.assertEquals( null, service.findByName( "Coffee" ), "The name DNE." );
        Assertions.assertEquals( null, service.findByName( "Cafe" ), "The name DNE." );
    }

    /**
     * Testing deleting all recipes at once
     */
    @Test
    @Transactional
    public void testDeleteAll () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 5 ) );
        r2.addIngredient( new Ingredient( "Milk", 3 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 1 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 2 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        Assertions.assertEquals( "Coffee", service.findByName( "Coffee" ).getName(), "The name is correct." );
        Assertions.assertEquals( "Mocha", service.findByName( "Mocha" ).getName(), "The name is correct." );
        Assertions.assertEquals( "Latte", service.findByName( "Latte" ).getName(), "The name is correct." );

        service.deleteAll();

        Assertions.assertEquals( null, service.findByName( "Coffee" ), "The name DNE." );
        Assertions.assertEquals( null, service.findByName( "Mocha" ), "The name DNE." );
        Assertions.assertEquals( null, service.findByName( "Latte" ), "The name DNE." );

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    /**
     * Testing deleting recipes one at a time
     */
    @Test
    @Transactional
    public void testDeleteRecipesOneByOne () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 5 ) );
        r2.addIngredient( new Ingredient( "Milk", 3 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 1 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 2 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );
        Assertions.assertEquals( "Coffee", service.findByName( "Coffee" ).getName(), "The name is correct." );
        Assertions.assertEquals( "Mocha", service.findByName( "Mocha" ).getName(), "The name is correct." );
        Assertions.assertEquals( "Latte", service.findByName( "Latte" ).getName(), "The name is correct." );

        service.delete( r1 );

        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );
        Assertions.assertEquals( null, service.findByName( "Coffee" ), "The name is correct." );
        Assertions.assertEquals( "Mocha", service.findByName( "Mocha" ).getName(), "The name is correct." );
        Assertions.assertEquals( "Latte", service.findByName( "Latte" ).getName(), "The name is correct." );

        service.delete( r2 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );
        Assertions.assertEquals( null, service.findByName( "Coffee" ), "The name is correct." );
        Assertions.assertEquals( null, service.findByName( "Mocha" ), "The name is correct." );
        Assertions.assertEquals( "Latte", service.findByName( "Latte" ).getName(), "The name is correct." );

        service.delete( r3 );

        Assertions.assertEquals( 0, service.count(), "All recipes should be gone from the database" );

        Assertions.assertNull( service.findByName( "Coffee" ), "All recipes should be gone from the database" );
        Assertions.assertNull( service.findByName( "Mocha" ), "All recipes should be gone from the database" );
        Assertions.assertNull( service.findByName( "Latte" ), "All recipes should be gone from the database" );
    }

    /**
     * Tests editing the price
     */
    @Test
    @Transactional
    public void testEditPrice () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        service.save( r1 );

        List<Ingredient> ingredients = r1.getIngredients();

        Assertions.assertEquals( 1, (int) r1.getPrice() );
        Assertions.assertEquals( 3, (int) ingredients.get( 0 ).getAmount() );
        Assertions.assertEquals( "Coffee", ingredients.get( 0 ).getName() );
        Assertions.assertEquals( 1, (int) ingredients.get( 1 ).getAmount() );
        Assertions.assertEquals( "Milk", ingredients.get( 1 ).getName() );
        Assertions.assertEquals( 1, (int) ingredients.get( 2 ).getAmount() );
        Assertions.assertEquals( "Sugar", ingredients.get( 2 ).getName() );

        r1.setPrice( 2 );
        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );
        ingredients = retrieved.getIngredients();

        Assertions.assertEquals( 2, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, (int) ingredients.get( 0 ).getAmount() );
        Assertions.assertEquals( "Coffee", ingredients.get( 0 ).getName() );
        Assertions.assertEquals( 1, (int) ingredients.get( 1 ).getAmount() );
        Assertions.assertEquals( "Milk", ingredients.get( 1 ).getName() );
        Assertions.assertEquals( 1, (int) ingredients.get( 2 ).getAmount() );
        Assertions.assertEquals( "Sugar", ingredients.get( 2 ).getName() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    /**
     * Update R2 into R1. Recipe is completely updated by new reference, not
     * ind. ingredient. Test case update for Ingredient functionality.
     */
    @Test
    @Transactional
    public void testEditRecipe () {
        Assertions.assertEquals( 0, service.count() );

        final Recipe r1 = new Recipe();
        r1.setName( "Blonde Roast" );
        r1.setPrice( 6 );
        r1.addIngredient( new Ingredient( "Coffee", 5 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Sugar", 2 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r1 );

        Recipe saved = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( r1.getName(), saved.getName() );
        Assertions.assertEquals( r1.getPrice(), saved.getPrice() );
        Assertions.assertEquals( r1.getIngredients(), saved.getIngredients() );

        final Recipe r2 = new Recipe();
        r2.setName( "Dark Roast" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 6 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );

        r1.updateRecipe( r2 );

        service.save( r1 );

        saved = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( r1.getName(), saved.getName() ); // Same name
        Assertions.assertEquals( r2.getPrice(), saved.getPrice() ); // New price
        Assertions.assertEquals( r2.getIngredients(), saved.getIngredients() ); // new
                                                                                // ingredients

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 1, recipes.size(),
                "Creating two recipes but there should still be 1 in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    /**
     * Test adding an ingredient.
     */
    @Test
    @Transactional
    public void testAddIngredientToRecipe () {
        Assertions.assertEquals( 0, service.count() );

        final Recipe r1 = new Recipe();
        r1.setName( "Blonde Roast" );
        r1.setPrice( 6 );
        r1.addIngredient( new Ingredient( "Coffee", 5 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Sugar", 2 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count() );
        Recipe r1Remote = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( 4, r1Remote.getIngredients().size() );
        Assertions.assertEquals( "Coffee", r1Remote.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( "Milk", r1Remote.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Sugar", r1Remote.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( "Chocolate", r1Remote.getIngredients().get( 3 ).getName() );

        // now add, then save to DB
        r1.addIngredient( new Ingredient( "Pumpkin Spice", 4 ) );
        service.save( r1 );
        Assertions.assertEquals( 1, service.count() );
        r1Remote = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( 5, r1Remote.getIngredients().size() );
        Assertions.assertEquals( "Coffee", r1Remote.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( "Milk", r1Remote.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Sugar", r1Remote.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( "Chocolate", r1Remote.getIngredients().get( 3 ).getName() );
        Assertions.assertEquals( "Pumpkin Spice", r1Remote.getIngredients().get( 4 ).getName() );

    }

    /**
     * Test remove an ingredient.
     */
    @Test
    @Transactional
    public void testRemoveIngredientFromRecipe () {

        Assertions.assertEquals( 0, service.count() );

        final Recipe r1 = new Recipe();
        r1.setName( "Blonde Roast" );
        r1.setPrice( 6 );
        final Ingredient iCoffee = new Ingredient( "Coffee", 5 );
        r1.addIngredient( iCoffee );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Sugar", 2 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count() );
        Recipe r1Remote = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( 4, r1Remote.getIngredients().size() );
        Assertions.assertEquals( "Coffee", r1Remote.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( "Milk", r1Remote.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Sugar", r1Remote.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( "Chocolate", r1Remote.getIngredients().get( 3 ).getName() );

        // now remove, then save to DB
        r1.removeIngredient( iCoffee );
        service.save( r1 );
        Assertions.assertEquals( 1, service.count() );
        r1Remote = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( 3, r1Remote.getIngredients().size() );
        Assertions.assertEquals( "Milk", r1Remote.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( "Sugar", r1Remote.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Chocolate", r1Remote.getIngredients().get( 2 ).getName() );
        // coffee DNE

        // Try to remove ingredient that isn't in recipe (does nothing)
        r1.removeIngredient( iCoffee );
        service.save( r1 );
        Assertions.assertEquals( 1, service.count() );
        r1Remote = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( 3, r1Remote.getIngredients().size() );
        Assertions.assertEquals( "Milk", r1Remote.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( "Sugar", r1Remote.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Chocolate", r1Remote.getIngredients().get( 2 ).getName() );

    }

    /**
     * Test updating unit of an ingredient.
     */
    @Test
    @Transactional
    public void testUpdateIngredient () {
        Assertions.assertEquals( 0, service.count() );

        final Recipe r1 = new Recipe();
        r1.setName( "Blonde Roast" );
        r1.setPrice( 6 );
        r1.addIngredient( new Ingredient( "Coffee", 5 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Sugar", 2 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count() );
        Recipe r1Remote = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( 4, r1Remote.getIngredients().size() );
        Assertions.assertEquals( "Coffee", r1Remote.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( "Milk", r1Remote.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Sugar", r1Remote.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( "Chocolate", r1Remote.getIngredients().get( 3 ).getName() );

        // now remove, then save to DB
        r1.updateIngredient( "Coffee", new Ingredient( "Coffee", 50 ) );
        service.save( r1 );
        Assertions.assertEquals( 1, service.count() );
        r1Remote = service.findByName( "Blonde Roast" );
        Assertions.assertEquals( 4, r1Remote.getIngredients().size() );
        Assertions.assertEquals( "Coffee", r1Remote.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( 50, r1Remote.getIngredients().get( 0 ).getAmount() ); // there
                                                                                       // is
                                                                                       // a
                                                                                       // change
        Assertions.assertEquals( "Milk", r1Remote.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Sugar", r1Remote.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( "Chocolate", r1Remote.getIngredients().get( 3 ).getName() );

    }

    /**
     * Gabriel's 3nd enchancement by equals call on recipe
     *
     * @throws Exception
     *             ie
     */
    @Test
    @Transactional
    public void testEquals () throws Exception {

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r2 = new Recipe( "Mocha", 50 );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );

        final Recipe r3 = new Recipe( "Latte", 60 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 2 ) );

        final Recipe r1Copy = new Recipe( "Coffee", 50 );
        r1Copy.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1Copy.addIngredient( new Ingredient( "Milk", 1 ) );
        r1Copy.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r2Copy = new Recipe( "Mocha", 50 );
        r2Copy.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2Copy.addIngredient( new Ingredient( "Milk", 1 ) );
        r2Copy.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2Copy.addIngredient( new Ingredient( "Chocolate", 2 ) );

        final Recipe r3Copy = new Recipe( "Latte", 60 );
        r3Copy.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3Copy.addIngredient( new Ingredient( "Milk", 2 ) );
        r3Copy.addIngredient( new Ingredient( "Sugar", 2 ) );

        final Recipe r1CopyNull = new Recipe( null, 50 );
        r1CopyNull.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1CopyNull.addIngredient( new Ingredient( "Milk", 1 ) );
        r1CopyNull.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r2CopyNull = new Recipe( null, 50 );
        r2CopyNull.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2CopyNull.addIngredient( new Ingredient( "Milk", 1 ) );
        r2CopyNull.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2CopyNull.addIngredient( new Ingredient( "Chocolate", 2 ) );

        final Recipe r3CopyNull = new Recipe( null, 60 );
        r3CopyNull.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3CopyNull.addIngredient( new Ingredient( "Milk", 2 ) );
        r3CopyNull.addIngredient( new Ingredient( "Sugar", 2 ) );

        final Inventory ie = new Inventory();

        // false on a different object
        Assertions.assertFalse( r1.equals( ie ) );

        Assertions.assertFalse( r2.equals( ie ) );

        Assertions.assertFalse( r3.equals( ie ) );

        // true on same object
        Assertions.assertTrue( r1.equals( r1 ) );

        Assertions.assertTrue( r2.equals( r2 ) );

        Assertions.assertTrue( r3.equals( r3 ) );

        // true same on a copy
        Assertions.assertTrue( r1.equals( r1Copy ) );

        Assertions.assertTrue( r2.equals( r2Copy ) );

        Assertions.assertTrue( r3.equals( r3Copy ) );

        // false on same copy but null name
        Assertions.assertFalse( r1.equals( r1CopyNull ) );

        Assertions.assertFalse( r2.equals( r2CopyNull ) );

        Assertions.assertFalse( r3.equals( r3CopyNull ) );

        // false on same copy but null name -- opposite
        Assertions.assertFalse( r1CopyNull.equals( r1 ) );

        Assertions.assertFalse( r2CopyNull.equals( r2 ) );

        Assertions.assertFalse( r3CopyNull.equals( r3 ) );

        // false on other objects
        Assertions.assertFalse( r1.equals( r2 ) );
        Assertions.assertFalse( r1.equals( r3 ) );

        Assertions.assertFalse( r2.equals( r1 ) );
        Assertions.assertFalse( r2.equals( r3 ) );

        Assertions.assertFalse( r3.equals( r1 ) );
        Assertions.assertFalse( r3.equals( r2 ) );

    }
}
