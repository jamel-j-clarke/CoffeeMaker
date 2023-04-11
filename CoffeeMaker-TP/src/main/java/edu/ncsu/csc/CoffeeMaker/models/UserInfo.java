package edu.ncsu.csc.CoffeeMaker.models;

/**
 * This class is used to temporarily hold user log in and sign up data when
 * passed by API calls before their objects are made. This is to prevent the
 * password from being hashed twice and to check the inputs before they are
 * saved.
 *
 * @author Erin Grouge
 *
 */
public class UserInfo {

    /** The user's email */
    private final String email;

    /** The user's name */
    private final String name;

    /** The user's password */
    private final String password;

    /**
     * Constructs an object to store user email, name, and password
     *
     * @param email
     *            the user's email
     * @param name
     *            the user's name
     * @param password
     *            the user's password
     */
    public UserInfo ( final String email, final String name, final String password ) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    /**
     * Returns the user's password
     *
     * @return password
     */
    public String getPassword () {
        return password;
    }

    /**
     * Returns the user's email
     *
     * @return email
     */
    public String getEmail () {
        return email;
    }

    /**
     * Returns the user's name
     *
     * @return name
     */
    public String getName () {
        return name;
    }
}
