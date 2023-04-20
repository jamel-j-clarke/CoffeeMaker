package edu.ncsu.csc.CoffeeMaker.models.users;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

import javax.management.InvalidAttributeValueException;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;

/**
 * Implements a basic User object for the CoffeeMaker system
 *
 * @author Emma Holincheck
 * @author Erin Grouge
 * @version 04/10/2023
 */
@MappedSuperclass
public class User extends DomainObject {
    /** The id of the user */
    @Id
    @GeneratedValue
    private Long   id;
    /** The name of the user */
    private String name;
    /** The email of the user */
    private String email;
    /** The password of the user */
    private String password;
    /** Salt used for hashing password */
    private String salt;

    /**
     * Constructs a new user object
     *
     * @param email
     *            of the user
     * @param name
     *            of the user
     * @param password
     *            for the user's account
     * @throws InvalidAttributeValueException
     *             if the email is invalid
     * @throws NoSuchAlgorithmException
     *             if there is an error
     * @throws InvalidKeySpecException
     *             if there is an error
     */
    public User ( final String email, final String name, final String password )
            throws InvalidAttributeValueException, InvalidKeySpecException, NoSuchAlgorithmException {
        // Create Salt
        final SecureRandom random = new SecureRandom();
        final byte[] s = new byte[16];
        random.nextBytes( s );
        salt = new String( s, StandardCharsets.UTF_8 );

        if ( ! ( setEmail( email ) && setName( name ) && setPassword( password ) ) ) {
            throw new InvalidAttributeValueException( "Invalid Input" );
        }

    }

    /**
     * Returns true if the password matches the manager's password
     *
     * @param p
     *            the password to check
     * @return true if the password matches
     */
    public boolean checkPassword ( final String p ) {
        final String passwordAttempt = hash( this.salt + p );

        return this.password.equals( passwordAttempt );
    }

    /**
     * Hashes the parameter
     *
     * Citation: The PackScheduler Project from CSC 216/217
     *
     * @param pw
     *            the password to hash
     * @return the hashed password
     */
    private String hash ( final String pw ) {
        try {
            final MessageDigest digest1 = MessageDigest.getInstance( "SHA-256" );
            digest1.update( ( pw ).getBytes() );
            return new String( digest1.digest() );
        }
        catch ( final NoSuchAlgorithmException e ) {
            throw new IllegalArgumentException( "Cannot hash password" );
        }
    }

    /**
     * Returns the User's salt. Used for testing purpose
     *
     * @return the User's salt
     */
    public String getSalt () {
        return this.salt;
    }

    /**
     * Constructs a new user object
     *
     */
    public User () {
        super();
    }

    /**
     * Helper method that validates the email and sets it if valid
     *
     * @param email
     *            of the user
     * @return true if the email was set, false if it was invalid
     */
    private boolean setEmail ( final String email ) {
        if ( email == null || email.trim().equals( "" ) ) {
            return false;
        }
        else if ( !email.contains( "@" ) || !email.contains( "." ) || email.indexOf( "." ) - email.indexOf( "@" ) <= 1
                || email.indexOf( "@" ) == 0 || email.lastIndexOf( "." ) == email.length() - 1 ) {
            // throw new InvalidAttributeValueException( "not a valid emails" );
            return false;
        }
        else {
            this.email = email;
            return true;
        }

    }

    /**
     * Updates the user's name and password with the new user name and password
     *
     * @param user
     *            the new user information
     * @return true if it was updated, false if not.
     */
    public boolean updateUser ( final User user ) {
        final String oldname = this.name;
        final String oldpassword = this.password;
        if ( setName( user.getName() ) ) {
            if ( user.getPassword() != null ) {
                this.password = user.getPassword();
                this.salt = user.getSalt();
                return true;
            }
            else {
                setName( oldname );
                return false;
            }
        }
        else { // If invalid, change back to old name and password
            setName( oldname );
            this.password = oldpassword;
            return false;
        }
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
     * Sets the users name
     *
     * @param name
     *            the name of the user
     *
     * @return true if the name was set, false if it was invalid
     */
    private boolean setName ( final String name ) {
        if ( name != null && !name.trim().equals( "" ) ) {
            this.name = name.trim();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Sets the users password
     *
     * @param password
     *            the password of the user
     * @return true if the password is set, false if not
     */
    private boolean setPassword ( final String password ) {
        if ( password != null && !password.trim().equals( "" ) ) {
            this.password = hash( this.salt + password );
            return true;
        }
        else {
            return false;
        }
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
        return this.id;
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
                && Objects.equals( password, other.password ) && Objects.equals( id, other.id );
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
