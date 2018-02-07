package io.github.joaofso.bookstore.control;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.control.dao.UserDAO;
import io.github.joaofso.bookstore.control.dao.impl.UserDAOXML;
import io.github.joaofso.bookstore.model.User;

/**
 * This class controls the UserDAO to perform the bookstore activities.
 * Furthermore, it separates the model layer from the view layer. It has the
 * capabilities to provide the operations over the users, inserting the security
 * layer, abstracting the storage mechanism.
 * 
 * @author João Felipe
 *
 */
public class UserController {

	private static final String INVALID_PASSWORD = "The provided password does not match. Try again providing the correct one!";
	private static final String GENERAL_ERROR = "Some error happened during the last action!";

	private UserDAO userDAO;

	/**
	 * Constructor of a user controller.
	 * @throws BookstoreException 
	 */
	public UserController() throws BookstoreException {
		this.userDAO = new UserDAOXML();
	}

	/**
	 * Performs the login of an user in the bookstore system.
	 * 
	 * @param username The username of the user to be logged in.
	 * @param password The password to be checked against.
	 * @return The instance of the logged user.
	 * @throws BookstoreException Thrown if the password with the 
	 * correspondent username is not registered or if the password 
	 * does not match.
	 */
	public User loginUser(String username, String password) throws BookstoreException {
		this.validateUsername(username);

		User user = this.userDAO.retrieveUser(username);

		String hashedPassword = this.hashPassword(password);

		if (user != null && this.passwordMatch(user, hashedPassword)) {
			return user;
		} else {
			throw new BookstoreException(INVALID_PASSWORD);
		}
	}

	/**
	 * Creates a new user in the book store system.
	 * @param username The username of the new user.
	 * @param password The password provided by the new user.
	 * @param admin A boolean value indicating whether this user is an administer in the system.
	 * @return The instance of the created user.
	 * @throws BookstoreException Thrown if any malformation rule is broken or any database problem happens.
	 */
	public User createUser(String username, String password, boolean admin) throws BookstoreException {
		this.validateUsername(username);
		this.validatePassword(password);

		User newUser = new User(username, password, admin);
		boolean success = this.userDAO.addUser(newUser);
		if (success) {
			return newUser;
		} else {
			throw new BookstoreException(GENERAL_ERROR);
		}
	}

	/**
	 * Deletes the user with the correspondent username and password.
	 * @param username The username of the user to be deleted.
	 * @param password The password of the user to be deleted.
	 * @return A boolean value indicating whether the user was successfully deleted.
	 * @throws BookstoreException Thrown if any malformation rule is broken or any database problem happens.
	 */
	public boolean deteleUser(String username, String password) throws BookstoreException {
		this.validateUsername(username);
		this.validatePassword(password);

		User user = this.userDAO.retrieveUser(username);

		String hashedPassword = this.hashPassword(password);

		if (user != null) {
			if (this.passwordMatch(user, hashedPassword)) {
				this.userDAO.removeUser(user);
				return true;
			} else {
				throw new BookstoreException(INVALID_PASSWORD);
			}

		}
		return false;
	}

	private String hashPassword(String password) {
		// Add here the hashing mechanism for the user password! I don't add any since
		// there is no need for the assignment
		return password;
	}

	private boolean passwordMatch(User user, String hashedPassword) {
		return user.getPassword().equals(hashedPassword);
	}

	private void validateUsername(String username) {
		// Add any malformation rules regarding username format here! I don't add any
		// since there is no need for the assignment
	}

	private void validatePassword(String password) {
		// Add any malformation rules regarding password format here! I don't add any
		// since there is no need for the assignment
	}

}
