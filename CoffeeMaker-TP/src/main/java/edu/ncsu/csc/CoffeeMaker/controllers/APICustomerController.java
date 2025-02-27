package edu.ncsu.csc.CoffeeMaker.controllers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.management.InvalidAttributeValueException;

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

import edu.ncsu.csc.CoffeeMaker.models.UserInfo;
import edu.ncsu.csc.CoffeeMaker.models.users.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Users.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Jonathan Kurian
 * @author Erin Grouge
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
        return null == user
                ? new ResponseEntity( errorResponse( "No customer found with id " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to a specific employee, as
     * indicated by the path variable provided (the id of the employee desired)
     *
     * @param email
     *            customer email
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/customers/email/{email}" )
    public ResponseEntity getCustomer ( @PathVariable ( "email" ) final String email ) {
        final Customer user = customerService.findByEmail( email );
        return null == user
                ? new ResponseEntity( errorResponse( "No customer found with email " + email ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( new UserInfo( user.getEmail(), user.getName(), "" ), HttpStatus.OK );
    }

    /**
     * Validates the customer trying to sign in.
     *
     * @param attempt
     *            the login attempt
     * @return success if the customer's email and password is correct, error if
     *         not.
     * @throws InvalidAttributeValueException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     * @throws NoSuchAlgorithmException
     *             if there is an error
     */
    @PostMapping ( BASE_PATH + "/customers/validate" )
    public ResponseEntity validateCustomer ( @RequestBody final UserInfo attempt )
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        final Customer user = customerService.findByEmail( attempt.getEmail() );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No customer found with email " + attempt.getEmail() ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            if ( user.checkPassword( attempt.getPassword() ) ) {
                return new ResponseEntity( new UserInfo( user.getEmail(), user.getName(), "" ), HttpStatus.OK );
            }
            else {
                return new ResponseEntity( "Incorrect password", HttpStatus.CONFLICT );
            }
        }
    }

    /**
     * REST API method to provide POST access to the Employee model. This is
     * used to create a new Employee by automatically converting the JSON
     * RequestBody provided to a Employee object. Invalid JSON will fail.
     *
     * @param customer
     *            The valid customer to be saved.
     * @return ResponseEntity indicating success if the User could be saved to
     *         the database, or an error if it could not be
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    @PostMapping ( BASE_PATH + "/customers" )
    public ResponseEntity createCustomer ( @RequestBody final UserInfo customer )
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        Customer cust;
        try {
            cust = new Customer( customer.getEmail(), customer.getName(), customer.getPassword() );
        }
        catch ( final InvalidAttributeValueException e ) {
            return new ResponseEntity( errorResponse( "Invalid input." ), HttpStatus.CONFLICT );
        }
        if ( null != customerService.findByEmail( customer.getEmail() ) ) {
            return new ResponseEntity(
                    errorResponse( "Customer with the email " + customer.getEmail() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        customerService.save( cust );
        return new ResponseEntity( successResponse( customer.getName() + " was successfully created" ), HttpStatus.OK );

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
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    @PutMapping ( BASE_PATH + "/customers/{id}" )
    public ResponseEntity updateCustomer ( @PathVariable final long id, @RequestBody final Customer customer )
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        Customer cust;
        try {
            cust = new Customer( customer.getEmail(), customer.getName(), customer.getPassword() );
        }
        catch ( final InvalidAttributeValueException e ) {
            return new ResponseEntity( errorResponse( "Invalid input. " ), HttpStatus.CONFLICT );
        }
        final Customer currUser = customerService.findById( id );

        if ( currUser == null ) {
            return new ResponseEntity( errorResponse( "Customer with the id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            currUser.updateUser( cust );
            customerService.save( currUser );
            return new ResponseEntity( successResponse( customer.getName() + " was successfully updated" ),
                    HttpStatus.OK );
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
            return new ResponseEntity( errorResponse( "No customer found for id " + id ), HttpStatus.NOT_FOUND );
        }
        customerService.delete( user );

        return new ResponseEntity( successResponse( user.getName() + " was deleted successfully" ), HttpStatus.OK );
    }

}
