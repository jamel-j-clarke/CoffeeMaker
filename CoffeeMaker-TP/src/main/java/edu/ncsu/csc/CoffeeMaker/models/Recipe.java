package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 * @author Erin Grouge
 * @author Gabriel Watts
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long             id;

    /** Recipe name */
    private String           name;

    /** Recipe price */
    @Min ( 0 )
    private Integer          price;

    /** The list of ingredients in the recipe */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /** Description of the Recipe for frontend */
    private String           description;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Creates a default recipe for the coffee maker.
     *
     * @param name
     *            the recipe name
     * @param price
     *            the recipe price
     */
    public Recipe ( final String name, final Integer price ) {
        setName( name );
        setPrice( price );
        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Returns the recipe description
     *
     * @return description
     */
    public String getDescription () {
        this.description = this.toString();
        return this.description;
    }

    /**
     *
     * Updates the price and ingredients to be equal to the passed Recipe
     *
     * @param r
     *            with updated price and ingredients
     */
    public void updateRecipe ( final Recipe r ) {
        setPrice( r.getPrice() );
        ingredients = r.getIngredients();

    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Adds the ingredient to the list of ingredients for the recipe
     *
     * @param ingredient
     *            the ingredient to add to the recipe
     */
    public void addIngredient ( final Ingredient ingredient ) {
        final int index = ingredients.indexOf( ingredient );
        // If doesn't already exist in the list, add.
        if ( index == -1 ) {
            ingredients.add( ingredient );
        }

    }

    /**
     * Remove the ingredient from the list of ingredients for the recipe.
     * Checking for if Ingredient is is in ingredients list is not necessary as
     * the user is only displayed contained ingredients.
     *
     * @param ingredient
     *            the ingredient to remove from the recipe
     */
    public void removeIngredient ( final Ingredient ingredient ) {
        final int index = ingredients.indexOf( ingredient );

        // If found, remove it
        if ( index != -1 ) {
            ingredients.remove( index ); // performs remove and a left shift if
                                         // not last element
        }

    }

    /**
     * Edits the units of an ingredient given, changes the amount of ingredient.
     *
     * @param name
     *            the name of the ingredient to update
     *
     * @param ingredient
     *            that INCLUDES new units for the current instance of
     *            ingredient.
     */
    public void updateIngredient ( final String name, final Ingredient ingredient ) {
        final Ingredient i = getIngredient( name );
        // If doesn't exist, do nothing
        if ( i == null ) {
            return;
        }

        final int index = ingredients.indexOf( i );
        // If found, update it
        if ( index != -1 ) {
            ingredients.get( index ).setAmount( ingredient.getAmount() );
        }

    }

    /**
     * Returns the recipe's list of ingredients
     *
     * @return ingredients
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
     * Returns the name of the recipe and a list of its ingredients.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuilder str = new StringBuilder();
        // Formatting for 1 ingredient
        if ( ingredients.size() == 1 ) {
            str.append( ingredients.get( 0 ).getName() );
            str.append( " (" );
            str.append( ingredients.get( 0 ).getAmount() );
            str.append( ")." );
            return str.toString();
        }
        // Formatting for 2 ingredients
        else if ( ingredients.size() == 2 ) {
            str.append( ingredients.get( 0 ).getName() );
            str.append( " (" );
            str.append( ingredients.get( 0 ).getAmount() );
            str.append( ") and " );
            str.append( ingredients.get( 1 ).getName() );
            str.append( " (" );
            str.append( ingredients.get( 1 ).getAmount() );
            str.append( ")." );
            return str.toString();
        }
        // Formatting for 3+ ingredients.
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( i == ingredients.size() - 1 ) {
                str.append( "and " );
                str.append( ingredients.get( i ).getName() );
                str.append( " (" );
                str.append( ingredients.get( i ).getAmount() );
                str.append( ")." );
            }
            else {
                str.append( ingredients.get( i ).getName() );
                str.append( " (" );
                str.append( ingredients.get( i ).getAmount() );
                str.append( "), " );
            }
        }
        return str.toString();

    }

    /**
     * Hash function for the Recipe object
     *
     * @return int hash for the recipe
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    /**
     * Returns true if the object is equal to this object
     *
     * @returns true if the objects are equal
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
