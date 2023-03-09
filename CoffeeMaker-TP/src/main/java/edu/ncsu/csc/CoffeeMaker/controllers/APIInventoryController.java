package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Inventory.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIInventoryController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService service;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. This will convert the Inventory to JSON.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity getInventory () {
        final Inventory inventory = service.getInventory();
        return new ResponseEntity( inventory, HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param inventory
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity updateInventory ( @RequestBody final Inventory inventory ) {
        final Inventory inventoryCurrent = service.getInventory();
        inventoryCurrent.addIngredients( inventory.getIngredients() );
        service.save( inventoryCurrent );
        return new ResponseEntity( inventoryCurrent, HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param ingredient
     *            amounts to add to inventory
     * @return response to the request
     */
    @PostMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity addIngredient ( @RequestBody final Ingredient ingredient ) {
        final Inventory inventoryCurrent = service.getInventory();
        final Ingredient i = inventoryCurrent.getIngredient( ingredient.getName() );
        if ( i == null ) {
            inventoryCurrent.addIngredient( ingredient );
            service.save( inventoryCurrent );
        }
        else {
            return new ResponseEntity( errorResponse( "Inventory already has " + ingredient.getName() ),
                    HttpStatus.CONFLICT );
        }

        return new ResponseEntity( successResponse( ingredient.getName() + " successfully added to Inventory" ),
                HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param name
     *            the name of the ingredient to add to
     * @param ingredient
     *            ingredient to update in inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/inventory/{name}" )
    public ResponseEntity updateIngredient ( @PathVariable ( "name" ) final String name,
            @RequestBody final Ingredient ingredient ) {
        final Inventory inventoryCurrent = service.getInventory();
        final Ingredient i = inventoryCurrent.getIngredient( name );
        if ( i == null ) {
            return new ResponseEntity( errorResponse( "Inventory does not have " + name + " to update" ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            inventoryCurrent.addIngredient( ingredient );
            service.save( inventoryCurrent );
        }

        return new ResponseEntity( successResponse( ingredient.getName() + " successfully updated in Inventory" ),
                HttpStatus.OK );
    }
}
