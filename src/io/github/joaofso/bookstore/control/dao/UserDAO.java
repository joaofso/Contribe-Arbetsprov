package io.github.joaofso.bookstore.control.dao;

import io.github.joaofso.bookstore.model.User;

/**
 * This interface defines the behavior of an object that manipulates directly
 * the users in the persistence layer.
 * 
 * @author João Felipe
 *
 */
public interface UserDAO {

	/**
	 * Adds a new user in the database
	 * 
	 * @param newUser
	 *            The new user to be added.
	 * @return A boolean value indicating whether the new user was added properly.
	 */
	boolean addUser(User newUser);

	/**
	 * Retrieves a user by its user name.
	 * 
	 * @param username
	 *            The user name of the user to be retrieved.
	 * @return The user instance related to the provided user name.
	 */
	User retrieveUser(String username);

	/**
	 * Removes the provided user from the database.
	 * 
	 * @param user The user to be removed.
	 * @return A boolean value indicating whether the user was removed from the
	 *         database properly.
	 */
	boolean removeUser(User user);

}