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

import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Users.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Jonathan Kurian
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIUserController extends APIController {

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService service;

    /**
     * REST API method to provide GET access to all users in the system
     *
     * @return JSON representation of all users
     */
    @GetMapping ( BASE_PATH + "/users" )
    public List<User> getUsers () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific user, as indicated by
     * the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/users/{email}" )
    public ResponseEntity getUser ( @PathVariable ( "email" ) final String email ) {
        final User user = service.findByEmail( email );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with email " + email ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the User model. This is used to
     * create a new User by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid User to be saved.
     * @return ResponseEntity indicating success if the User could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final User user ) {
        if ( null != service.findByEmail( user.getEmail() ) ) {
            return new ResponseEntity( errorResponse( "User with the name " + user.getEmail() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( service.findAll().size() < 3 ) {
            service.save( user );
            return new ResponseEntity( successResponse( user.getName() + " successfully created" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse( "Insufficient space in user book for user " + user.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

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
    @PutMapping ( BASE_PATH + "/users/{id}" )
    public ResponseEntity updateUser ( @PathVariable final long id, @RequestBody final User user ) {
        final User currUser = service.findById( id );

        if ( currUser == null ) {
            return new ResponseEntity( errorResponse( "User with the id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            currUser.updateRecipe( user );
            service.save( currUser );
            return new ResponseEntity( successResponse( user + " successfully updated" ), HttpStatus.OK );
        }

    }

    /**
     * REST API method to allow deleting a User from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the user to delete (as a path variable)
     *
     * @param email
     *            The email of the user to delete
     * @return Success if the user could be deleted; an error if the user does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/users/{email}" )
    public ResponseEntity deleteUser ( @PathVariable final String email ) {
        final User user = service.findByEmail( email );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No user found for email " + email ), HttpStatus.NOT_FOUND );
        }
        service.delete( user );

        return new ResponseEntity( successResponse( user + " was deleted successfully" ), HttpStatus.OK );
    }

}
