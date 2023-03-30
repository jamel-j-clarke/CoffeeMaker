package edu.ncsu.csc.CoffeeMaker.models.users;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.EmployeeService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

public class Manager extends Employee {

    static InventoryService inventory;
    static RecipeService    recipes;
    static EmployeeService  employees;

    public Manager ( final String email, final String name, final String password ) {
        super( email, name, password );
        // TODO Auto-generated constructor stub
    }

    public static void addIngredient ( final Ingredient ingredient ) {
        final Inventory ivt = inventory.getInventory();
        ivt.addIngredient( ingredient );
        inventory.save( ivt );
    }

    public static void addRecipe ( final Recipe recipe ) {

    }

    public static void editRecipe ( final String name, final Recipe recipe ) {

    }

    public static void deleteRecipe ( final String name ) {

    }

    public static void createEmployeeAccount ( final String email, final String name, final String password ) {

    }
}
