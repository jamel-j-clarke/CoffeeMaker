package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository is used to provide CRUD operations for the User model. Spring
 * will generate appropriate code with JPA.
 *
 * @author Jonathan Kurian
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User object with the provided email. Spring will generate code to
     * make this happen.
     *
     * @param email
     *            Email of the User
     * @return Found User, null if none.
     */
    User findByEmail ( String email );

}
