package edu.ncsu.csc.CoffeeMaker.models.users;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.management.InvalidAttributeValueException;

/**
 * The Manager User in the system that controls inventory, recipe, and personnel
 * maintenance
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/20/2023
 *
 */
public class Manager extends Employee {

    /** The name of the user */
    private final String  name     = "Manager";
    /** The email of the user */
    private static String email    = "m4n4g3r@csc326.edu";
    /** The password of the user */
    private static String password = "tuffyhunttalleyhill";

    /**
     * Constructs a new Manger user
     *
     * @throws InvalidAttributeValueException
     *             if the managers email is invalid
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    public Manager () throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        super( "m4n4g3r@csc326.edu", "Manager", "tuffyhunttalleyhill" );
    }

    /**
     * Returns true if the email is the manager's email
     *
     * @param e
     *            the email to check
     * @return true if the email's match
     */
    public static boolean checkEmail ( final String e ) {
        return email.equals( e );
    }

}
