package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * Tests the Ingredient Class and its methods. Implemented to allow for UC7
 * functionality.
 *
 * @author Erin Grouge
 * @author Gabriel Watts
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    /** The service object used to store the recipes */
    @Autowired
    private IngredientService service;

    /**
     * Deletes all previously stored recipes before each test
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Test Constructing an ingredient object with both constructors. By Erin.
     */
    @Test
    @Transactional
    public void testIngredient () {
        // use constructor with arguments
        final Ingredient i1 = new Ingredient( "Coffee", 5 );

        Assertions.assertEquals( 5, i1.getAmount() );
        Assertions.assertEquals( "Coffee", i1.getName() );

        Assertions.assertEquals( "Ingredient [name=Coffee, amount=5]", i1.toString() );

        // use constructor with NO arguments
        final Ingredient i2 = new Ingredient();
        i2.setAmount( 5 );
        i2.setName( "Coffee" );

        Assertions.assertEquals( 5, i2.getAmount() );
        Assertions.assertEquals( "Coffee", i2.getName() );

        Assertions.assertEquals( "Ingredient [name=Coffee, amount=5]", i2.toString() );

    }

    /**
     * Test Adding an ingredient object with to the database. Invalid unit
     * AddIngredient tested. For implementation deleteAll() is also
     * incorporated. By Gabe.
     */
    @Test
    @Transactional
    public void testAddIngredient () {
        assertEquals( 0, service.count() );

        // initialize two Ingredients
        final Ingredient iCoffeeValid = new Ingredient( "Coffee", 5 );
        service.save( iCoffeeValid );
        assertEquals( 1, service.count() );

        final Ingredient iMIlkValid = new Ingredient( "Milk", 10 );
        service.save( iMIlkValid );
        assertEquals( 2, service.count() );

        // check contents in DB
        final List<Ingredient> ingredients = service.findAll();
        assertEquals( 2, ingredients.size() );
        assertEquals( iCoffeeValid, ingredients.get( 0 ) );
        assertEquals( iMIlkValid, ingredients.get( 1 ) );

        service.deleteAll();
        assertEquals( 0, service.count() );
        // INVALID unit
        final Ingredient iPSInvalid = new Ingredient( "Pumpkin Spice", -10 );
        try {
            service.save( iPSInvalid );

        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }
        // SINCE "CURRUPT" AFTER SAVE INVALID DUPLICATE IS TESTED IN NEW METHOD
        /*
         * // INVALID duplicate service.save( iCoffeeValid ); // save a valid
         * coffee assertEquals( 1, service.count() ); final Ingredient
         * iCoffeeValid2 = new Ingredient( "Coffee", 2 ); // save // another //
         * coffee // (duplicate). try { service.save( iCoffeeValid2 ); } catch (
         * final ConstraintViolationException cvee ) { // expected }
         */

    }

    /**
     * Test Adding a ingredients objects with to the database. Multiple at once.
     * Invalid unit/ duplicate AddIngredient tested. For implementation
     * deleteAll() is also incorporated. By Gabe.
     */
    @Test
    @Transactional
    public void testAddIngredients () {
        assertEquals( 0, service.count() );

        // initialize two Ingredients
        final Ingredient iCoffeeValid = new Ingredient( "Coffee", 5 );
        final Ingredient iMilkValid = new Ingredient( "Milk", 10 );

        final List<Ingredient> ingredients = List.of( iCoffeeValid, iMilkValid );
        service.saveAll( ingredients );

        // check
        assertEquals( 2, service.count() );
        final List<Ingredient> ingredientsDB = service.findAll();
        assertEquals( 2, ingredientsDB.size() );
        assertEquals( iCoffeeValid, ingredientsDB.get( 0 ) );
        assertEquals( iMilkValid, ingredientsDB.get( 1 ) );

        service.deleteAll();
        assertEquals( 0, service.count() );
        // INVALID CASE - invalid Unit
        final Ingredient iCoffeeInvalid = new Ingredient( "Coffee", -5 );
        final List<Ingredient> ingredientsInvalid = List.of( iCoffeeInvalid, iMilkValid );
        try {
            service.saveAll( ingredientsInvalid );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

        // INVALID CASE - duplicate Unit
        final Ingredient iCoffeeValid2 = new Ingredient( "Coffee", 5 );
        final List<Ingredient> ingredientsInvalid2 = List.of( iCoffeeValid, iCoffeeValid2 );
        try {
            service.saveAll( ingredientsInvalid2 );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where both ingredients have the same name" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }
    }

    /**
     * Test Delete an ingredients objects from the database. One at a time. By
     * Gabe.
     */
    @Test
    @Transactional
    public void testDeleteIngredientsOneByOne () {
        assertEquals( 0, service.count() );

        // initialize two Ingredients
        final Ingredient iCoffeeValid = new Ingredient( "Coffee", 5 );
        service.save( iCoffeeValid );
        assertEquals( 1, service.count() );

        final Ingredient iMilkValid = new Ingredient( "Milk", 10 );
        service.save( iMilkValid );
        assertEquals( 2, service.count() );

        // now delete the first ingredient
        service.delete( iCoffeeValid );
        assertEquals( 1, service.count() );
        final List<Ingredient> iList = service.findAll();
        assertEquals( "Milk", iList.get( 0 ).getName(), "The only ingredient in service should be milk" );

        // delete only ingredient
        service.delete( iMilkValid );
        assertEquals( 0, service.count() );
        final List<Ingredient> iList2 = service.findAll();
        assertEquals( 0, iList2.size(), "The are no ingredients in DB" );

    }

    /**
     * Test updating an ingredient object to the database. One at once. Invalid
     * unit/ duplicate tested. By Gabe.
     */
    @Test
    @Transactional
    public void testUpdateIngredient () {
        assertEquals( 0, service.count() );

        // initialize two Ingredients
        final Ingredient iCoffeeValid = new Ingredient( "Coffee", 5 );
        assertEquals( "Coffee", iCoffeeValid.getName() );
        assertEquals( 5, iCoffeeValid.getAmount() );
        service.save( iCoffeeValid );
        assertEquals( 1, service.count() );

        final Ingredient iMilkValid = new Ingredient( "Milk", 10 );
        assertEquals( "Milk", iMilkValid.getName() );
        assertEquals( 10, iMilkValid.getAmount() );
        service.save( iMilkValid );
        assertEquals( 2, service.count() );

        // change the TYPE of coffee to chocolate, keep milk exactly the same
        iCoffeeValid.setName( "Chocolate" );
        service.save( iCoffeeValid );
        // "local"
        assertEquals( "Chocolate", iCoffeeValid.getName() );
        assertEquals( 5, iCoffeeValid.getAmount() );
        // DB copy
        // final Ingredient iCoffeeValidDB = service.findAll().get( 0 );
        final Ingredient iCoffeeValidDB = service.findByName( "Chocolate" );
        assertEquals( "Chocolate", iCoffeeValidDB.getName() );
        assertEquals( 5, iCoffeeValidDB.getAmount() );
        assertEquals( 2, service.count(), "Editing an ingredient shouldn't duplicate it" );
        // milk is the same
        final Ingredient iMilkValidDB = service.findByName( "Milk" );
        assertEquals( "Milk", iMilkValidDB.getName() );
        assertEquals( 10, iMilkValidDB.getAmount() );

        // change the AMOUNT of chocolate (was coffee), keep milk exactly the
        // same
        iCoffeeValid.setAmount( 50 );
        service.save( iCoffeeValid );
        // "local"
        assertEquals( "Chocolate", iCoffeeValid.getName() );
        assertEquals( 50, iCoffeeValid.getAmount() );
        // DB copy
        final Ingredient iCoffeeValidDB50 = service.findByName( "Chocolate" );
        assertEquals( "Chocolate", iCoffeeValidDB50.getName() );
        assertEquals( 50, iCoffeeValidDB50.getAmount() );
        assertEquals( 2, service.count(), "Editing an ingredient shouldn't duplicate it" );
        // milk is the same
        final Ingredient iMilkValidDB2 = service.findByName( "Milk" );
        assertEquals( "Milk", iMilkValidDB2.getName() );
        assertEquals( 10, iMilkValidDB2.getAmount() );

    }

    /**
     * Tests saving with an invalid amount
     */
    @Test
    @Transactional
    public void testInvalidAmount () {
        assertEquals( 0, service.count() );

        // initialize two Ingredients
        final Ingredient iCoffeeValid = new Ingredient( "Coffee", -5 );
        try {
            service.save( iCoffeeValid );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where both ingredients have the same name" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

        iCoffeeValid.setAmount( 5 );
        service.save( iCoffeeValid );
        assertEquals( 1, service.count() );

        iCoffeeValid.setAmount( -5 );
        try {
            service.save( iCoffeeValid );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where both ingredients have the same name" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    /**
     * Checks that similar Ingredients are not the same, but the same
     * ingredients do show up as equal
     *
     * @throws Exception
     *             ie
     */
    @Test
    @Transactional
    public void testEquals () throws Exception {

        final Ingredient i1 = new Ingredient( "Sugar", 3 );
        final Ingredient i2 = new Ingredient( "Sugar", 3 );
        final Ingredient i3 = new Ingredient( "Coffee", 7 );
        final Ingredient i1Copy = i1;

        Assertions.assertEquals( i1.getAmount(), i2.getAmount() );
        Assertions.assertEquals( i1.getName(), i2.getName() );
        Assertions.assertTrue( i1.equals( i2 ) );
        Assertions.assertTrue( i1.equals( i1Copy ) );
        Assertions.assertFalse( i1.equals( i3 ) );

    }

}
