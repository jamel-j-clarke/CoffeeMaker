package edu.ncsu.csc.CoffeeMaker.models.users;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.EmployeeService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * The Manager User in the system that controls inventory, recipe, and personnel
 * maintenance
 *
 * @author Emma Holincheck
 * @version 03/30/2023
 *
 */
public class Manager extends Employee {

    /** Manager id */
    @Id
    @GeneratedValue
    static long             id;
    /** The current system inventory */
    static InventoryService inventory;
    /** The current system recipes */
    static RecipeService    recipes;
    /** The current system employees */
    static EmployeeService  employees;

    /**
     * Constructs a new Manger user
     *
     * @param email
     *            the email of the manager user
     * @param name
     *            the name of the manager user
     * @param password
     *            the password of the manager user
     */
    public Manager ( final String email, final String name, final String password ) {
        super( email, name, password );
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredient
     *            the ingredient to be added to the inventory
     */
    public static void addIngredient ( final Ingredient ingredient ) {
        final Inventory ivt = inventory.getInventory();
        ivt.addIngredient( ingredient );
        inventory.save( ivt );
    }

    /**
     * Allows the Manager to add a recipe to the system
     *
     * @param recipe
     *            the recipe being added to the system
     */
    public static void addRecipe ( final Recipe recipe ) {
        recipes.save( recipe );
    }

    /**
     * Allows the Manager to edit an existing recipe in the system
     *
     * @param name
     *            the name of the recipe
     * @param recipe
     *            the new recipe object
     */
    public static void editRecipe ( final String name, final Recipe recipe ) {
        recipes.save( recipe );
    }

    /**
     * Allows the Manager to delete a recipe from the system
     *
     * @param recipe
     */
    public static void deleteRecipe ( final Recipe recipe ) {
        recipes.delete( recipe );
    }

    /**
     * Allows the Manager to create a new employee user in the system
     *
     * @param email
     *            the email of the new employee
     * @param name
     *            the name of the new employee
     * @param password
     *            the password of the new employee
     */
    public static void createEmployeeAccount ( final String email, final String name, final String password ) {
        final Employee temp = new Employee( email, name, password );
        employees.save( temp );
    }
}
