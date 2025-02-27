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
import edu.ncsu.csc.CoffeeMaker.models.users.Employee;
import edu.ncsu.csc.CoffeeMaker.models.users.Manager;
import edu.ncsu.csc.CoffeeMaker.services.EmployeeService;

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
public class APIEmployeeController extends APIController {

    /**
     * EmployeeService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private EmployeeService employeeService;

    /**
     * REST API method to provide GET access to all employees in the system
     *
     * @return JSON representation of all users
     */
    @GetMapping ( BASE_PATH + "/employees" )
    public List<Employee> getEmployees () {
        return employeeService.findAll();
    }

    /**
     * REST API method to provide GET access to a specific employee, as
     * indicated by the path variable provided (the id of the employee desired)
     *
     * @param id
     *            employee id
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/employees/{id}" )
    public ResponseEntity getEmployee ( @PathVariable ( "id" ) final long id ) {
        final Employee user = employeeService.findById( id );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No employee found with id " + id ), HttpStatus.NOT_FOUND );
        }
        else {
            return new ResponseEntity( user, HttpStatus.OK );
        }
    }

    /**
     * REST API method to provide GET access to a specific employee, as
     * indicated by the path variable provided (the id of the employee desired)
     *
     * @param email
     *            employee email
     * @return response to the request
     * @throws InvalidAttributeValueException
     *             if there is an error
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    @GetMapping ( BASE_PATH + "/employees/email/{email}" )
    public ResponseEntity getEmployee ( @PathVariable final String email )
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        if ( Manager.checkEmail( email ) ) {
            final Manager man = new Manager();
            return new ResponseEntity( new UserInfo( man.getEmail(), man.getName(), "" ), HttpStatus.ACCEPTED );
        }
        final Employee user = employeeService.findByEmail( email );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No employee found with email " + email ), HttpStatus.NOT_FOUND );
        }
        else {
            return new ResponseEntity( new UserInfo( user.getEmail(), user.getName(), "" ), HttpStatus.OK );
        }
    }

    /**
     * Validates the employee trying to sign in.
     *
     * @param attempt
     *            the login in attempt
     * @return success if the employee's email and password is correct, error if
     *         not.
     * @throws InvalidAttributeValueException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     * @throws NoSuchAlgorithmException
     *             if there is an error
     */
    @PostMapping ( BASE_PATH + "/employees/validate" )
    public ResponseEntity validateEmployee ( @RequestBody final UserInfo attempt )
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        if ( Manager.checkEmail( attempt.getEmail() ) ) {
            final Manager man = new Manager();
            if ( man.checkPassword( attempt.getPassword() ) ) {
                return new ResponseEntity( new UserInfo( man.getEmail(), man.getName(), "" ), HttpStatus.ACCEPTED );
            }
            return new ResponseEntity( errorResponse( "Invalid password." ), HttpStatus.CONFLICT );
        }
        final Employee user = employeeService.findByEmail( attempt.getEmail() );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No employee found with email " + attempt.getEmail() ),
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
     * @param employee
     *            The valid User to be saved.
     * @return ResponseEntity indicating success if the User could be saved to
     *         the database, or an error if it could not be
     * @throws NoSuchAlgorithmException
     *             if there's an error
     * @throws InvalidKeySpecException
     *             if there's an error
     */
    @PostMapping ( BASE_PATH + "/employees" )
    public ResponseEntity createEmployee ( @RequestBody final UserInfo employee )
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        // Check for invalid inputs with temporary employee object
        Employee emp;
        try {
            emp = new Employee( employee.getEmail(), employee.getName(), employee.getPassword() );
        }
        catch ( final InvalidAttributeValueException e ) {
            return new ResponseEntity( errorResponse( "Invalid input." ), HttpStatus.CONFLICT );
        }
        if ( null != employeeService.findByEmail( employee.getEmail() ) ) {
            return new ResponseEntity(
                    errorResponse( "Employee with the email " + employee.getEmail() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        employeeService.save( emp );
        return new ResponseEntity( successResponse( employee.getName() + " successfully created" ), HttpStatus.OK );

    }

    /**
     * REST API method to provide PUT access to the Employee model. This is used
     * to edit a Employee by automatically converting the JSON RequestBody
     * provided to a Employee object. Invalid JSON will fail.
     *
     * @param employee
     *            The valid Employee to be saved.
     * @param id
     *            the id of the employee to edit
     *
     * @return ResponseEntity indicating success if the Employee could be saved
     *         to the inventory, or an error if it could not be
     * @throws NoSuchAlgorithmException
     *             if there's an error
     * @throws InvalidKeySpecException
     *             if there's an error
     */
    @PutMapping ( BASE_PATH + "/employees/{id}" )
    public ResponseEntity updateEmployeeById ( @PathVariable final long id, @RequestBody final Employee employee )
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        // Check for invalid inputs with temporary employee object
        Employee emp;
        try {
            emp = new Employee( employee.getEmail(), employee.getName(), employee.getPassword() );
        }
        catch ( final InvalidAttributeValueException e ) {
            return new ResponseEntity( errorResponse( "Invalid input." ), HttpStatus.CONFLICT );
        }
        final Employee currUser = employeeService.findById( id );

        if ( currUser == null ) {
            return new ResponseEntity( errorResponse( "Employee with the id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        if ( currUser.updateUser( emp ) ) {
            employeeService.save( currUser );
            return new ResponseEntity( successResponse( employee.getName() + " was successfully updated" ),
                    HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse( "Invalid name or password." ), HttpStatus.CONFLICT );
        }

    }

    /**
     * REST API method to allow deleting a Employee from the CoffeeMaker's
     * Database, by making a DELETE request to the API endpoint and indicating
     * the user to delete (as a path variable)
     *
     * @param id
     *            The id of the user to delete
     * @return Success if the user could be deleted; an error if the user does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/employees/{id}" )
    public ResponseEntity deleteEmployee ( @PathVariable final long id ) {
        final Employee user = employeeService.findById( id );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No employee found for id " + id ), HttpStatus.NOT_FOUND );
        }
        employeeService.delete( user );

        return new ResponseEntity( successResponse( user + " was deleted successfully" ), HttpStatus.OK );
    }

}
