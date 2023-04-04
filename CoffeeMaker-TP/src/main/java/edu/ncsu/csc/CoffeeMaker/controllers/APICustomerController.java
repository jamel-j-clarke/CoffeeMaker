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
public class APICustomerController extends APIController {

    /**
     * CustomerService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private CustomerService customerService;

    /**
     * REST API method to provide GET access to all employees in the system
     *
     * @return JSON representation of all users
     */
    @GetMapping ( BASE_PATH + "/customers" )
    public List<Customer> getCustomers () {
        return customerService.findAll();
    }

    /**
     * REST API method to provide GET access to a specific employee, as
     * indicated by the path variable provided (the id of the employee desired)
     *
     * @param id
     *            customer id
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/customers/{id}" )
    public ResponseEntity getCustomer ( @PathVariable ( "id" ) final long id ) {
        final Customer user = customerService.findById( id );
        return null == user ? new ResponseEntity( errorResponse( "No user found with id " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Employee model. This is
     * used to create a new Employee by automatically converting the JSON
     * RequestBody provided to a Employee object. Invalid JSON will fail.
     *
     * @param employee
     *            The valid User to be saved.
     * @return ResponseEntity indicating success if the User could be saved to
     *         the database, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/customers" )
    public ResponseEntity createCustomer ( @RequestBody final Customer customer ) {
        if ( null != customerService.findById( customer.getId() ) ) {
            return new ResponseEntity( errorResponse( "User with the name " + customer.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        customerService.save( customer );
        return new ResponseEntity( successResponse( customer.getName() + " successfully created" ), HttpStatus.OK );

    }

    /**
     * REST API method to provide PUT access to the Employee model. This is used
     * to edit a Customer by automatically converting the JSON RequestBody
     * provided to a Employee object. Invalid JSON will fail.
     *
     * @param customer
     *            The valid Customer to be saved.
     * @param id
     *            the id of the customer to edit
     *
     * @return ResponseEntity indicating success if the Customer could be saved
     *         to the inventory, or an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/customers/{id}" )
    public ResponseEntity updateCustomer ( @PathVariable final long id, @RequestBody final Customer customer ) {
        final Customer currUser = customerService.findById( id );

        if ( currUser == null ) {
            return new ResponseEntity( errorResponse( "User with the id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            currUser.updateCustomer( customer );
            customerService.save( currUser );
            return new ResponseEntity( successResponse( customer + " successfully updated" ), HttpStatus.OK );
        }

    }

    /**
     * REST API method to allow deleting a Customer from the CoffeeMaker's
     * Database, by making a DELETE request to the API endpoint and indicating
     * the user to delete (as a path variable)
     *
     * @param id
     *            The id of the user to delete
     * @return Success if the user could be deleted; an error if the user does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/customers/{id}" )
    public ResponseEntity deleteCustomer ( @PathVariable final long id ) {
        final Customer user = customerService.findById( id );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No user found for id " + id ), HttpStatus.NOT_FOUND );
        }
        customerService.delete( user );

        return new ResponseEntity( successResponse( user + " was deleted successfully" ), HttpStatus.OK );
    }

}
