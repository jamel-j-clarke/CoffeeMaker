package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * The Ingredient Class represents an arbitrary recipe ingredient and its amount
 * in the recipe.
 *
 * @author Erin Grouge
 * @author Gabriel Watts
 *
 */
@Entity
public class Ingredient extends DomainObject {

    /** Ingredient id */
    @Id
    @GeneratedValue
    private Long    id;

    /** The name of ingredient */
    private String  name;

    /** The amount of the ingredient */
    @Min ( 0 )
    private Integer amount;

    /**
     * Constructs a new Ingredient
     *
     * @param name
     *            the name of ingredient
     * @param amount
     *            the amount of it
     */
    public Ingredient ( final String name, final Integer amount ) {
        super();
        setName( name );
        setAmount( amount );
    }

    /**
     * Constructs a new Ingredient with default values
     */
    public Ingredient () {
        super();
        name = "";
    }

    /**
     * Returns the ingredient name
     *
     * @return name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the ingredient name
     *
     * @param name
     *            the new name
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the amount of the ingredient
     *
     * @return amount
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the ingredient. Note, similar to Recipe setPrice(),
     * units are not checked yet here.
     *
     * @param amount
     *            the new amount
     */
    public void setAmount ( final Integer amount ) {
        this.amount = amount;

    }

    /**
     * Sets the ingredient ID
     *
     * @param id
     *            the id number
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the ID
     *
     * @return id
     */
    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Returns a string version of the ingredients information
     */
    @Override
    public String toString () {
        return "Ingredient [name=" + name + ", amount=" + amount + "]";
    }

    @Override
    public int hashCode () {
        return Objects.hash( amount, name );
    }

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
        final Ingredient other = (Ingredient) obj;
        return Objects.equals( name, other.name );
    }

}
