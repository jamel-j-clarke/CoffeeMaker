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

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.users.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Users.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Jonathan Kurian
 * @author Erin Grouge
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIOrderController extends APIController {

    /**
     * OrderService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private OrderService     orderService;

    /**
     * OrderService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private InventoryService inventoryService;

    /**
     * OrderService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private CustomerService  customerService;

    /**
     * OrderService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private RecipeService    recipeService;

    /**
     * REST API method to provide GET access to all orders in the system
     *
     * @return JSON representation of all users
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<Order> getOrders () {
        return orderService.findAll();
    }

    /**
     * REST API method to provide GET access to all orders in the system
     *
     * @param email
     *            the user's email
     *
     * @return JSON representation of all users
     */
    @GetMapping ( BASE_PATH + "/orders/customer/{email}" )
    public List<Order> getCustomerOrders ( @PathVariable final String email ) {
        return orderService.findByStatusNotAndUserEmail( OrderStatus.PICKED_UP, email );
    }

    /**
     * REST API method to provide GET access to incomplete orders
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/incomplete" )
    public List<Order> getIncompleteOrders () {
        return orderService.findByStatus( OrderStatus.NOT_STARTED );
    }

    /**
     * REST API method to provide GET access to orders in progress
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/inprogress" )
    public List<Order> getInprogressOrders () {
        return orderService.findByStatus( OrderStatus.IN_PROGRESS );
    }

    /**
     * REST API method to provide GET access to completed orders
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/completed" )
    public List<Order> getCompletedOrders () {
        return orderService.findByStatus( OrderStatus.DONE );
    }

    /**
     * REST API method to provide GET access to picked up orders
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/pickedup" )
    public List<Order> getPickedUpOrders () {
        return orderService.findByStatus( OrderStatus.PICKED_UP );
    }

    /**
     * REST API method to provide GET access to a specific order, as indicated
     * by the path variable provided (the id of the order desired)
     *
     * @param id
     *            order id
     * @return response to the request
     * @throws InvalidAttributeValueException
     *             if there is an error
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    @GetMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity getOrder ( @PathVariable final long id )
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {

        final Order order = orderService.findById( id );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found with order " + id ), HttpStatus.NOT_FOUND );
        }
        else {
            return new ResponseEntity( order, HttpStatus.OK );
        }
    }

    /**
     * REST API method to provide POST access to the Order model. This is used
     * to create a new Order by automatically converting the JSON RequestBody
     * provided to a Order object. Invalid JSON will fail.
     *
     * @param order
     *            The valid Order to be saved.
     * @return ResponseEntity indicating success if the Order could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity createOrder ( @RequestBody final Order order ) {
        final Customer cust = customerService.findByEmail( order.getUserEmail() );
        final Order o;
        try {
            o = new Order( order.getRecipe(), order.getPayment(), order.getUserEmail() );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Order info invalid." ), HttpStatus.CONFLICT );
        }
        orderService.save( o );
        // cust.orderBeverage( o );
        return new ResponseEntity( o.getId(), HttpStatus.OK );

    }

    /**
     * REST API method to provide PUT access to the Order model. This is used to
     * start a Order by automatically converting the JSON RequestBody provided
     * to a Order object. Invalid JSON will fail.
     *
     * @param id
     *            the id of the order to edit
     *
     * @return ResponseEntity indicating success if the Order could be updated
     *         or an error if it could not be
     * @throws NoSuchAlgorithmException
     *             if there's an error
     * @throws InvalidKeySpecException
     *             if there's an error
     */
    @PutMapping ( BASE_PATH + "/orders/start/{id}" )
    public ResponseEntity startOrder ( @PathVariable final long id )
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        final Order currOrder = orderService.findById( id );

        if ( currOrder == null ) {
            return new ResponseEntity( errorResponse( "Order with the id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        if ( currOrder.getStatus() != OrderStatus.NOT_STARTED ) {
            return new ResponseEntity( errorResponse( "Order with the id " + id + " cannot be started" ),
                    HttpStatus.BAD_REQUEST );
        }

        else if ( inventoryService.getInventory()
                .enoughIngredients( recipeService.findByName( currOrder.getRecipe() ) ) ) {
            currOrder.start();
            orderService.save( currOrder );
            return new ResponseEntity( successResponse( currOrder.getId() + " was successfully started" ),
                    HttpStatus.OK );
        }

        else {
            return new ResponseEntity( errorResponse( currOrder.getId() + " does not have enough ingredients" ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * REST API method to provide PUT access to the Order model. This is used to
     * complete a Order by automatically converting the JSON RequestBody
     * provided to a Order object. Invalid JSON will fail.
     *
     * @param id
     *            the id of the order to edit
     *
     * @return ResponseEntity indicating success if the Order could be updated
     *         or an error if it could not be
     * @throws NoSuchAlgorithmException
     *             if there's an error
     * @throws InvalidKeySpecException
     *             if there's an error
     */
    @PutMapping ( BASE_PATH + "/orders/complete/{id}" )
    public ResponseEntity completeOrder ( @PathVariable final long id )
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        final Order currOrder = orderService.findById( id );

        if ( currOrder == null ) {
            return new ResponseEntity( errorResponse( "Order with the id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }

        final Recipe rec = recipeService.findByName( currOrder.getRecipe() );
        if ( rec == null ) {
            return new ResponseEntity( errorResponse( "Order with the recipe " + rec.getName() + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        final Inventory inv = inventoryService.getInventory();

        if ( currOrder.getStatus() != OrderStatus.IN_PROGRESS ) {
            return new ResponseEntity( errorResponse( "Order with the id " + id + " cannot be completed" ),
                    HttpStatus.BAD_REQUEST );
        }
        else if ( inventoryService.getInventory().enoughIngredients( rec ) ) {
            inv.useIngredients( rec );
            inventoryService.save( inv );
            currOrder.complete();
            orderService.save( currOrder );
            return new ResponseEntity( successResponse( currOrder.getId() + " was successfully completed" ),
                    HttpStatus.OK );
        }

        else {
            return new ResponseEntity( errorResponse( currOrder.getId() + " does not have enough ingredients" ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * REST API method to provide PUT access to the Order model. This is used to
     * pickup a Order by automatically converting the JSON RequestBody provided
     * to a Order object. Invalid JSON will fail.
     *
     * @param id
     *            the id of the order to edit
     *
     * @return ResponseEntity indicating success if the Order could be updated
     *         to the inventory, or an error if it could not be
     * @throws NoSuchAlgorithmException
     *             if there's an error
     * @throws InvalidKeySpecException
     *             if there's an error
     */
    @PutMapping ( BASE_PATH + "/orders/pickup/{id}" )
    public ResponseEntity pickupOrder ( @PathVariable final long id )
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        final Order currOrder = orderService.findById( id );

        if ( currOrder == null ) {
            return new ResponseEntity( errorResponse( "Order with the id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        if ( currOrder.getStatus() != OrderStatus.DONE ) {
            return new ResponseEntity( errorResponse( "Order with the id " + id + " cannot be picked  up" ),
                    HttpStatus.BAD_REQUEST );
        }
        else {
            currOrder.pickup();
            orderService.save( currOrder );
            // final Customer cust = customerService.findByEmail(
            // currOrder.getUserEmail() );
            // Removes the order from the list of current orders
            // cust.cancelOrder( currOrder );
            return new ResponseEntity( successResponse( currOrder.getId() + " was successfully picked up" ),
                    HttpStatus.OK );
        }

    }

    /**
     * REST API method to allow deleting a Order from the CoffeeMaker's
     * Database, by making a DELETE request to the API endpoint and indicating
     * the user to delete (as a path variable)
     *
     * @param id
     *            The id of the user to delete
     * @return Success if the user could be deleted; an error if the user does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/orders/customer/{id}" )
    public ResponseEntity cancelCustomerOrder ( @PathVariable final long id ) {
        final Order order = orderService.findById( id );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found for id " + id ), HttpStatus.NOT_FOUND );
        }

        if ( order.getStatus() == OrderStatus.NOT_STARTED ) {
            final String userEmail = order.getUserEmail();
            final Customer cust = customerService.findByEmail( userEmail );
            cust.cancelOrder( order );
            orderService.delete( order );
            return new ResponseEntity( successResponse( order + " was deleted successfully" ), HttpStatus.OK );
        }

        else {
            return new ResponseEntity( errorResponse( order + " was cannot be cancelled" ), HttpStatus.NOT_ACCEPTABLE );
        }

    }

    /**
     * REST API method to allow deleting a Order from the CoffeeMaker's
     * Database, by making a DELETE request to the API endpoint and indicating
     * the user to delete (as a path variable)
     *
     * @param id
     *            The id of the user to delete
     * @return Success if the user could be deleted; an error if the user does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/orders/employee/{id}" )
    public ResponseEntity cancelEmployeeOrder ( @PathVariable final long id ) {
        final Order order = orderService.findById( id );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found for id " + id ), HttpStatus.NOT_FOUND );
        }

        if ( order.getStatus() == OrderStatus.NOT_STARTED || order.getStatus() == OrderStatus.IN_PROGRESS ) {
            final String userEmail = order.getUserEmail();
            final Customer cust = customerService.findByEmail( userEmail );
            cust.cancelOrder( order );
            orderService.delete( order );
            return new ResponseEntity( successResponse( order + " was deleted successfully" ), HttpStatus.OK );
        }

        else {
            return new ResponseEntity( errorResponse( order + " was cannot be cancelled" ), HttpStatus.NOT_ACCEPTABLE );
        }

    }

}
