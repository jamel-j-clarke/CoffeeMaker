package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;
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
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This class tests the Inventory Class and its methods to ensure proper
 * functionality
 *
 * @author Erin Grouge
 * @author Gabriel Watts
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    /** Use to store the inventory */
    @Autowired
    private InventoryService inventoryService;

    /**
     * Initialize inventory to 500 of each ingredient
     */
    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Chocolate", 500 ) );
        ivt.addIngredient( new Ingredient( "Milk", 500 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 500 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 500 ) );

        inventoryService.save( ivt );
    }

    /**
     * Testing useIngredients
     */
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();
        Assertions.assertEquals( 4, i.getIngredients().size() );

        // Create recipe to consume
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setPrice( 5 );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );

        // Use the ingredients - should return true on success
        Assertions.assertTrue( i.useIngredients( recipe ) );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */
        final List<Ingredient> iList = i.getIngredients();
        Assertions.assertEquals( 490, (int) iList.get( 0 ).getAmount() );
        Assertions.assertEquals( 480, (int) iList.get( 1 ).getAmount() );
        Assertions.assertEquals( 495, (int) iList.get( 2 ).getAmount() );
        Assertions.assertEquals( 499, (int) iList.get( 3 ).getAmount() );

        // Test not enough ingredients
        recipe.getIngredients().get( 0 ).setAmount( 600 );
        recipe.getIngredients().get( 1 ).setAmount( 600 );
        recipe.getIngredients().get( 2 ).setAmount( 600 );
        recipe.getIngredients().get( 3 ).setAmount( 600 );

        // Should return false if unsuccessful
        Assertions.assertFalse( i.useIngredients( recipe ) );

        // nothing should be changed
        final List<Ingredient> listAfterUnsuccessful = i.getIngredients();
        Assertions.assertEquals( 490, (int) listAfterUnsuccessful.get( 0 ).getAmount() );
        Assertions.assertEquals( 480, (int) listAfterUnsuccessful.get( 1 ).getAmount() );
        Assertions.assertEquals( 495, (int) listAfterUnsuccessful.get( 2 ).getAmount() );
        Assertions.assertEquals( 499, (int) listAfterUnsuccessful.get( 3 ).getAmount() );

    }

    /**
     * Tests addIngredients()
     */
    @Test
    @Transactional
    public void testAddIngredients () {
        final Inventory i = inventoryService.getInventory();
        Assertions.assertEquals( 4, i.getIngredients().size() );

        final List<Ingredient> ing = new ArrayList<Ingredient>();
        ing.add( new Ingredient( "Coffee", 100 ) );
        ing.add( new Ingredient( "Milk", 100 ) );
        ing.add( new Ingredient( "Sugar", 100 ) );
        ing.add( new Ingredient( "Chocolate", 100 ) );

        i.addIngredients( ing );
        Assertions.assertEquals( 4, i.getIngredients().size() );
        Assertions.assertEquals( 600, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 600, i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 600, i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 600, i.getIngredients().get( 3 ).getAmount() );
    }

    /**
     * Tests addIngredients()
     */
    @Test
    @Transactional
    public void testAddNewIngredients () {
        final Inventory i = inventoryService.getInventory();
        Assertions.assertEquals( 4, i.getIngredients().size() );

        final List<Ingredient> ing = new ArrayList<Ingredient>();
        ing.add( new Ingredient( "Coffee", 100 ) );
        ing.add( new Ingredient( "Chai", 200 ) );
        ing.add( new Ingredient( "Caramel", 100 ) );

        i.addIngredients( ing );
        Assertions.assertEquals( 6, i.getIngredients().size() );
        Assertions.assertEquals( 500, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( "Chocolate", i.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( 500, i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( "Milk", i.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( 500, i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( "Sugar", i.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( 600, i.getIngredients().get( 3 ).getAmount() );
        Assertions.assertEquals( "Coffee", i.getIngredients().get( 3 ).getName() );
        Assertions.assertEquals( 200, i.getIngredients().get( 4 ).getAmount() );
        Assertions.assertEquals( "Chai", i.getIngredients().get( 4 ).getName() );
        Assertions.assertEquals( 100, i.getIngredients().get( 5 ).getAmount() );
        Assertions.assertEquals( "Caramel", i.getIngredients().get( 5 ).getName() );
    }

    /**
     * Tests addIngredients()
     */
    @Test
    @Transactional
    public void testDeleteIngredients () {
        final Inventory i = inventoryService.getInventory();

        Assertions.assertEquals( 4, i.getIngredients().size() );
        Assertions.assertEquals( 500, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( "Chocolate", i.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( 500, i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( "Milk", i.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( 500, i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( "Sugar", i.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( 500, i.getIngredients().get( 3 ).getAmount() );
        Assertions.assertEquals( "Coffee", i.getIngredients().get( 3 ).getName() );

        i.deleteIngredient( i.getIngredients().get( 0 ) );

        Assertions.assertEquals( 3, i.getIngredients().size() );
        Assertions.assertEquals( 500, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( "Milk", i.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( 500, i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( "Sugar", i.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( 500, i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( "Coffee", i.getIngredients().get( 2 ).getName() );

        i.deleteIngredient( i.getIngredients().get( 1 ) );

        Assertions.assertEquals( 2, i.getIngredients().size() );
        Assertions.assertEquals( 500, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( "Milk", i.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( 500, i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( "Coffee", i.getIngredients().get( 1 ).getName() );

        i.deleteIngredient( i.getIngredients().get( 1 ) );

        Assertions.assertEquals( 1, i.getIngredients().size() );
        Assertions.assertEquals( 500, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( "Milk", i.getIngredients().get( 0 ).getName() );

        i.deleteIngredient( i.getIngredients().get( 0 ) );

        Assertions.assertEquals( 0, i.getIngredients().size() );

    }

    /**
     * Test adding a valid amount of ingredients to the inventory. This is not
     * replacing amounts !
     */
    @Test
    @Transactional
    public void testAddInventoryValid () {
        Inventory ivt = inventoryService.getInventory();

        final Ingredient iChocolateValid = new Ingredient( "Chocolate", 5 );
        final Ingredient iMilkValid = new Ingredient( "Milk", 3 );
        final Ingredient iSugarValid = new Ingredient( "Sugar", 7 );
        final Ingredient iCoffeeValid = new Ingredient( "Coffee", 2 );

        ivt.addIngredients( List.of( iChocolateValid, iMilkValid, iSugarValid, iCoffeeValid ) );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getIngredients().get( 0 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for chocolate" );
        Assertions.assertEquals( 503, (int) ivt.getIngredients().get( 1 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getIngredients().get( 2 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getIngredients().get( 3 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values coffee" );

    }

    /**
     * Test enoughIngredients using sufficient and insufficient examples()
     */
    @Test
    @Transactional
    public void testEnoughIngredients () {
        // Inventory should have 500 of each ingredient
        final Inventory ivt = inventoryService.getInventory();

        // Create Recipe with ingredients less than what is in inventory
        final Recipe r = new Recipe();
        r.setName( "Coffee" );
        r.setPrice( 50 );
        r.addIngredient( new Ingredient( "Chocolate", 50 ) );
        r.addIngredient( new Ingredient( "Milk", 50 ) );
        r.addIngredient( new Ingredient( "Sugar", 50 ) );
        r.addIngredient( new Ingredient( "Coffee", 50 ) );

        // Check to make sure the recipe can be made
        Assertions.assertTrue( ivt.enoughIngredients( r ) );
        // Insufficient Coffee - not enough ingredients
        r.updateIngredient( "Coffee", new Ingredient( "Coffee", 600 ) );
        Assertions.assertFalse( ivt.enoughIngredients( r ) );

        // Change ingredient back to isolate testing of each ingredient
        r.updateIngredient( "Coffee", new Ingredient( "Coffee", 50 ) );

        // Check to make sure the recipe can be made
        Assertions.assertTrue( ivt.enoughIngredients( r ) );

        // Insufficient Milk - not enough ingredients
        r.updateIngredient( "Milk", new Ingredient( "Milk", 600 ) );
        Assertions.assertFalse( ivt.enoughIngredients( r ) );

        // Change ingredient back to isolate testing of each ingredient
        r.updateIngredient( "Milk", new Ingredient( "Milk", 50 ) );

        // Check to make sure the recipe can be made
        Assertions.assertTrue( ivt.enoughIngredients( r ) );

        // Insufficient Sugar - not enough ingredients
        r.updateIngredient( "Sugar", new Ingredient( "Sugar", 600 ) );
        Assertions.assertFalse( ivt.enoughIngredients( r ) );

        // Change ingredient back to isolate testing of each ingredient
        r.updateIngredient( "Sugar", new Ingredient( "Sugar", 50 ) );

        // Check to make sure the recipe can be made
        Assertions.assertTrue( ivt.enoughIngredients( r ) );

        // Insufficient Chocolate - not enough ingredients
        r.updateIngredient( "Chocolate", new Ingredient( "Chocolate", 600 ) );
        Assertions.assertFalse( ivt.enoughIngredients( r ) );

        // Change ingredient back to isolate testing of each ingredient
        r.updateIngredient( "Chocolate", new Ingredient( "Chocolate", 50 ) );
    }

    /**
     * Test the toString method
     */
    @Test
    @Transactional
    public void testToString () {
        // Should start with 500 of each ingredient
        final Inventory ivt = inventoryService.getInventory();

        // Check to string
        Assertions.assertEquals(
                "Inventory with ingredients [Ingredient [name=Chocolate, amount=500], Ingredient [name=Milk, amount=500], Ingredient [name=Sugar, amount=500], Ingredient [name=Coffee, amount=500]]",
                ivt.toString() );

        // Create a recipe to consume
        final Recipe r = new Recipe();
        r.setName( "Coffee" );
        r.addIngredient( new Ingredient( "Coffee", 50 ) );
        r.addIngredient( new Ingredient( "Milk", 50 ) );
        r.addIngredient( new Ingredient( "Sugar", 50 ) );
        r.addIngredient( new Ingredient( "Chocolate", 50 ) );
        r.setPrice( 50 );

        // Use ingredients
        ivt.useIngredients( r );

        // Check to see if it's updated
        Assertions.assertEquals(
                "Inventory with ingredients [Ingredient [name=Chocolate, amount=450], Ingredient [name=Milk, amount=450], Ingredient [name=Sugar, amount=450], Ingredient [name=Coffee, amount=450]]",
                ivt.toString() );

    }

}
