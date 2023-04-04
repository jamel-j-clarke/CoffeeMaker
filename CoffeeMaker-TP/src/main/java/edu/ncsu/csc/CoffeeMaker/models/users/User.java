package edu.ncsu.csc.CoffeeMaker.models.users;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;

/**
 * Implements a basic User object for the CoffeeMaker system
 *
 * @author Emma Holincheck
 * @version 04/03/2023
 */
@Entity // Not sure if we want this at this level but can check back on it
public class User extends DomainObject {
    /** The id of the user */
    private long         id;
    /** The name of the user */
    private final String name;
    /** The email of the user */
    private final String email;
    /** The password of the user */
    private final String password;

    /**
     * Constructs a new user object
     *
     * @param email
     *            of the user
     * @param name
     *            of the user
     * @param password
     *            for the user's account
     */
    public User ( final String email, final String name, final String password ) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    /**
     * Gets the users name
     *
     * @return the name of the user
     */
    public String getName () {
        return name;
    }

    /**
     * Gets the users email
     *
     * @return the email of the user
     */
    public String getEmail () {
        return email;
    }

    /**
     * Gets the users password
     *
     * @return the password of the user
     */
    public String getPassword () {
        return password;
    }

    /**
     * Adding this from the DomainObject
     */
    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Equals method for the user type
     *
     * @return true if the objects are equal and false if they are not
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
        final User other = (User) obj;
        return Objects.equals( name, other.name ) && Objects.equals( email, other.email )
                && Objects.equals( password, other.password );
    }

    /**
     * to String for the user object
     *
     * @return prints the users name and email
     */
    @Override
    public String toString () {
        String s = "";
        s += name + "\n";
        s += email + "\n";
        return s;
    }

}
