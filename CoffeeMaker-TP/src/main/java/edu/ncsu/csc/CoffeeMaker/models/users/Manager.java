package edu.ncsu.csc.CoffeeMaker.models.users;

import javax.management.InvalidAttributeValueException;

/**
 * The Manager User in the system that controls inventory, recipe, and personnel
 * maintenance
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/04/2023
 *
 */
public class Manager extends Employee {

    /**
     * Constructs a new Manger user
     *
     * @throws InvalidAttributeValueException
     *             if the managers email is invalid
     */
    public Manager () throws InvalidAttributeValueException {
        super( "m4n4g3r@csc326.edu", "Manager", "tuffyhunttallyhill" );
    }

    // /**
    // * Adds ingredients to the inventory
    // *
    // * @param ingredient
    // * the ingredient to be added to the inventory
    // */
    // public static void addIngredient ( final Ingredient ingredient ) {
    // final Inventory ivt = inventory.getInventory();
    // ivt.addIngredient( ingredient );
    // inventory.save( ivt );
    // }

    // /**
    // * Allows the Manager to add a recipe to the system
    // *
    // * @param recipe
    // * the recipe being added to the system
    // */
    // public static void addRecipe ( final Recipe recipe ) {
    // recipes.save( recipe );
    // }

    // /**
    // * Allows the Manager to edit an existing recipe in the system
    // *
    // * @param name
    // * the name of the recipe
    // * @param recipe
    // * the new recipe object
    // */
    // public static void editRecipe ( final String name, final Recipe recipe )
    // {
    // recipes.save( recipe );
    // }

    // /**
    // * Allows the Manager to delete a recipe from the system
    // *
    // * @param recipe
    // */
    // public static void deleteRecipe ( final Recipe recipe ) {
    // recipes.delete( recipe );
    // }

    // /**
    // * Allows the Manager to create a new employee user in the system
    // *
    // * @param email
    // * the email of the new employee
    // * @param name
    // * the name of the new employee
    // * @param password
    // * the password of the new employee
    // * @throws InvalidAttributeValueException
    // * if the employee's email is invalid
    // */
    // public static void createEmployeeAccount ( final String email, final
    // String name, final String password )
    // throws InvalidAttributeValueException {
    // final Employee temp = new Employee( email, name, password );
    // employees.save( temp );
    // }
}
