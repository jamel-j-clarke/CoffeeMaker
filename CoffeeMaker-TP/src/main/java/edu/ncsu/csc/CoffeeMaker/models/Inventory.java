package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 * @author Gabriel Watts
 * @author Erin Grouge
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** The list of ingredients in the inventory */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate. Ingredients is empty.
     */
    public Inventory () {
        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param ingr
     *            a list of ingredients that the inventory will have
     */
    public Inventory ( final List<Ingredient> ingr ) {
        ingredients = ingr;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Getter method for the ingredients in Inventory. Will be useful in
     * APIInventory updateInventory().
     *
     * @return a list of ingredients in inventory
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns the ingredient object with that name
     *
     * @param name
     *            the name of the ingredient to return
     * @return the ingredient
     */
    public Ingredient getIngredient ( final String name ) {
        int index = -1;
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( name ) ) {
                index = i;
                break;
            }
        }
        if ( index != -1 ) {
            return ingredients.get( index );
        }
        else {
            return null;
        }
    }

    /**
     * Returns true if there are enough ingredients to make the beverage. This
     * includes enough units and if ingredient is even in the inventory.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {

        for ( final Ingredient i : r.getIngredients() ) {
            final int index = indexOf( i );

            if ( index == -1 ) {
                return false;
            }
            else if ( i.getAmount() > this.ingredients.get( index ).getAmount() ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make.
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( final Ingredient ingredientFromRecipe : r.getIngredients() ) {
                final int index = indexOf( ingredientFromRecipe );
                if ( index == -1 ) {
                    return false;
                }
                updateAmount( index, ( -1 * ingredientFromRecipe.getAmount() ) );
            }

            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Helper method to update an ingredient's amount at the index given
     *
     * @param index
     *            the index of the ingredient to update
     * @param amount
     *            the amount to add
     */
    private void updateAmount ( final int index, final int amount ) {
        final int prevAmount = ingredients.get( index ).getAmount();
        ingredients.get( index ).setAmount( prevAmount + amount );
    }

    /**
     * Adds to the amount of several ingredients respective amounts, the
     * Ingredient objects will contain the amount that is to be added. An
     * ingredient object will already validate for negative values, thus not
     * needed here. NOTE: It is not necessary to allow for a new type of
     * ingredient as that is the functionality of addIngredient() UC7.
     *
     * @param ingredients
     *            contains ingredient amounts that is to be ADDED to the
     *            inventory.
     */
    public void addIngredients ( final List<Ingredient> ingredients ) {
        // the amount in each ingredient is the number that is to be ADDED to
        // current inventory of ingredient.
        for ( final Ingredient i : ingredients ) {
            addIngredient( i );
        }

    }

    /**
     * Adds the ingredient to inventory if it isn't previously added or updates
     * the amount if it already exists.
     *
     * @param ing
     *            the ingredient to add
     */
    public void addIngredient ( final Ingredient ing ) {
        final int index = indexOf( ing );
        // If not in current inventory, add it
        if ( index == -1 ) {
            ingredients.add( ing );
        }
        // If in inventory, update the amount
        else {
            updateAmount( index, ing.getAmount() );
        }
    }

    /**
     * Removes the ingredient from inventory
     *
     * @param ing
     *            the ingredient to delete
     */
    public void deleteIngredient ( final Ingredient ing ) {
        final int index = indexOf( ing );
        if ( index == -1 ) {
            // do nothing it doesn't exist
        }
        else {
            ingredients.remove( index );
        }
    }

    /**
     * Custom indexOf method because ArrayList's only returns correctly for the
     * exact same object, not just the same name.
     *
     * @param ingredient
     *            the ingredient to find
     * @return the index of it in the list
     */
    private int indexOf ( final Ingredient ingredient ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( ingredient.getName() ) ) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        buf.append( "Inventory with ingredients " );
        buf.append( ingredients.toString() );

        return buf.toString();
    }

}
