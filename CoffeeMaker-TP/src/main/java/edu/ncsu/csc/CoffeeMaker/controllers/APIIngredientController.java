package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController; // Currently only

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Ingredients.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author McKenna Corn
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * REST API method to allow getting all Ingredients from a CoffeeMaker's
     * Recipe, by making a GET request to the API endpoint
     *
     * @return JSON representation of all ingredients for recipe
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return ingredientService.findAll();
    }

    /**
     * REST API method to allow getting an Ingredient from CoffeeMaker's
     * Inventory, by making a GET request to the API endpoint and indicating the
     * ingredient to return (as a path variable)
     *
     * @param name
     *            object responsible for the name of the ingredient inside the
     *            recipe
     * @return a response entity that the ingredient was found
     */
    @GetMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable ( "name" ) final String name ) {
        final Ingredient i = ingredientService.findByName( name );
        return null == i
                ? new ResponseEntity( errorResponse( "No ingredient found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( i, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Ingredient model. This is
     * used to create a new Ingredient by automatically converting the JSON
     * RequestBody provided to a Ingredient object. Invalid JSON will fail.
     *
     * @param ingredient
     *            the valid Ingredient that is being saved.
     *
     * @return a ResponseEntity indicating success if the Ingredient could be
     *         saved to the inventory, or an error if it could not be.
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {

        final Ingredient ingr = ingredientService.findByName( ingredient.getName() );

        if ( null != ingr ) {
            return new ResponseEntity(
                    errorResponse( "Ingredient with the name " + ingredient.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        try {
            ingredientService.save( ingredient );
            return new ResponseEntity( successResponse( ingredient.getName() + " successfully created" ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST ); // HttpStatus.FORBIDDEN
                                                                 // would be OK
                                                                 // too.
        }
    }

    /**
     * REST API endpoint to provide update access to the Ingredient model. This
     * will update the Inventory and Recipes of the CoffeeMaker by changing
     * amounts and units from the Ingredient and changing in Inventory and
     * Recipes.
     *
     * @param ingredient
     *            new Ingredient object that is replacing the current one.
     *
     * @return a ResponseEntity indicating success if the Ingredient could be
     *         updated to the inventory and to the recipes, or an error if it
     *         could not be.
     */
    @PutMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity updateIngredient ( @RequestBody final Ingredient ingredient ) {

        final Ingredient ingr = ingredientService.findByName( ingredient.getName() );

        if ( null == ingr ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + ingredient.getName() ),
                    HttpStatus.NOT_FOUND );
        }

        ingr.setAmount( ingredient.getAmount() );
        ingredientService.save( ingr );
        return new ResponseEntity( successResponse( ingredient.getName() + " was updated successfully" ),
                HttpStatus.OK );
    }

    /**
     *
     * REST API method to allow deleting an Ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name
     *            the name of the Ingredient to delete
     *
     * @return Success if the ingredient could be deleted; an error if the
     *         ingredient does not exist or it could not be removed from
     *         inventory and/or all Recipes
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {

        final Ingredient ingr = ingredientService.findByName( name );

        if ( null == ingr ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
        }

        ingredientService.delete( ingr );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }
}
