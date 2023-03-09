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
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Recipes.
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
public class APIRecipeController extends APIController {

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService service;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all recipies
     */
    @GetMapping ( BASE_PATH + "/recipes" )
    public List<Recipe> getRecipes () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity getRecipe ( @PathVariable ( "name" ) final String name ) {
        final Recipe recipe = service.findByName( name );
        return null == recipe
                ? new ResponseEntity( errorResponse( "No recipe found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( recipe, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to ingredients in a recipe, as
     * indicated by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/recipes/{name}/ingredients" )
    public ResponseEntity getIngredients ( @PathVariable ( "name" ) final String name ) {
        final Recipe recipe = service.findByName( name );
        if ( recipe == null ) {
            return new ResponseEntity( errorResponse( "No recipe found with name " + name ), HttpStatus.NOT_FOUND );
        }
        final List<Ingredient> ingredients = recipe.getIngredients();
        if ( ingredients.size() == 0 ) {
            return new ResponseEntity( errorResponse( "Recipe found with name " + name + " has no ingredients" ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            return new ResponseEntity( ingredients, HttpStatus.OK );
        }

    }

    /**
     * REST API method to provide GET access to a specific ingredient in a
     * recipe, as indicated by the path variables provided (the name of the
     * recipe and ingredient desired)
     *
     * @param recipe
     *            recipe name
     * @param ingredient
     *            ingredient name
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/recipes/{rname}/ingredients/{iname}" )
    public ResponseEntity getIngredient ( @PathVariable ( "rname" ) final String recipe,
            @PathVariable ( "iname" ) final String ingredient ) {
        final Recipe r = service.findByName( recipe );
        if ( r == null ) {
            return new ResponseEntity( errorResponse( "No recipe found with name " + recipe ), HttpStatus.NOT_FOUND );
        }
        final Ingredient i = r.getIngredient( ingredient );
        if ( i == null ) {
            return new ResponseEntity( errorResponse( "No ingredient found with name " + ingredient ),
                    HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( i, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to create a new Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/recipes" )
    public ResponseEntity createRecipe ( @RequestBody final Recipe recipe ) {
        if ( null != service.findByName( recipe.getName() ) ) {
            return new ResponseEntity( errorResponse( "Recipe with the name " + recipe.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( service.findAll().size() < 3 ) {
            service.save( recipe );
            return new ResponseEntity( successResponse( recipe.getName() + " successfully created" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity(
                    errorResponse( "Insufficient space in recipe book for recipe " + recipe.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

    }

    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to add an ingredient to a recipe by automatically converting the JSON
     * RequestBody provided to a Recipe object. Invalid JSON will fail.
     *
     * @param rname
     *            The recipe to add the ingredient too
     * @param ingredient
     *            the ingredient to add
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/recipes/{rname}/ingredients" )
    public ResponseEntity addIngredient ( @PathVariable ( "rname" ) final String rname,
            @RequestBody final Ingredient ingredient ) {
        final Recipe recipe = service.findByName( rname );
        // If recipe does not exist, return error
        if ( recipe == null ) {
            return new ResponseEntity( errorResponse( "Recipe with the name " + rname + " does not exist" ),
                    HttpStatus.CONFLICT );
        }
        final Ingredient i = recipe.getIngredient( ingredient.getName() );
        // If ingredient doesn't already exist in recipe, add it
        if ( i == null ) {
            recipe.addIngredient( ingredient );
            service.save( recipe );
            return new ResponseEntity( successResponse( ingredient.getName() + " successfully added to " + rname ),
                    HttpStatus.OK );
        }
        // Otherwise, return error
        return new ResponseEntity(
                errorResponse( "Recipe with the name " + rname + " already has " + ingredient.getName() ),
                HttpStatus.CONFLICT );
    }

    /**
     * REST API method to provide PUT access to the Recipe model. This is used
     * to edit a Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @param name
     *            the name of the recipe to edit
     *
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity updateRecipe ( @PathVariable final String name, @RequestBody final Recipe recipe ) {
        final Recipe rec = service.findByName( name );

        if ( rec == null ) {
            return new ResponseEntity( errorResponse( "Recipe with the name " + name + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            rec.updateRecipe( recipe );
            service.save( rec );
            return new ResponseEntity( successResponse( name + " successfully updated" ), HttpStatus.OK );
        }

    }

    /**
     * REST API method to provide PUT access to the Recipe model. This is used
     * to edit a Recipe Ingredient by automatically converting the JSON
     * RequestBody provided to a Recipe object. Invalid JSON will fail.
     *
     *
     * @param rname
     *            the name of the recipe to edit
     * @param iname
     *            the ingredient to edit
     * @param ingredient
     *            the new ingredient to update it to
     *
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/recipes/{rname}/ingredients/{iname}" )
    public ResponseEntity updateIngredient ( @PathVariable ( "rname" ) final String rname,
            @PathVariable ( "iname" ) final String iname, @RequestBody final Ingredient ingredient ) {
        final Recipe rec = service.findByName( rname );

        if ( rec == null ) {
            return new ResponseEntity( errorResponse( "Recipe with the name " + rname + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        final Ingredient i = rec.getIngredient( iname );
        if ( i == null ) {
            return new ResponseEntity( errorResponse( "No ingredient found with name " + ingredient ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            rec.updateIngredient( iname, ingredient );
            service.save( rec );
            return new ResponseEntity( successResponse( iname + " successfully updated in " + rname ), HttpStatus.OK );
        }

    }

    /**
     * REST API method to allow deleting a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the recipe to delete (as a path variable)
     *
     * @param name
     *            The name of the Recipe to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity deleteRecipe ( @PathVariable final String name ) {
        final Recipe recipe = service.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( recipe );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow deleting an ingredient from a Recipe, by making
     * a DELETE request to the API endpoint and indicating the recipe to delete
     * (as a path variable)
     *
     * @param rname
     *            The name of the Recipe to delete ingredient from
     * @param iname
     *            The name of the ingredient to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/recipes/{rname}/ingredients/{iname}" )
    public ResponseEntity deleteIngredient ( @PathVariable ( "rname" ) final String rname,
            @PathVariable ( "iname" ) final String iname ) {
        final Recipe recipe = service.findByName( rname );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + rname ), HttpStatus.NOT_FOUND );
        }
        final Ingredient ingredient = recipe.getIngredient( iname );
        if ( ingredient == null ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + iname + " in " + rname ),
                    HttpStatus.NOT_FOUND );
        }
        recipe.removeIngredient( ingredient );
        service.save( recipe );

        return new ResponseEntity( successResponse( iname + " was deleted successfully from " + rname ),
                HttpStatus.OK );
    }
}
