package io.github.joaofso.bookstore.control.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.control.dao.UserDAO;
import io.github.joaofso.bookstore.model.User;

class UserDAOXMLTest {
	
	private final static String TEST_USER_FILE = "./TEST_UserDatabase.xml";

	@Test
	void testAddUser() {
		try {
			UserDAO userDAO = new UserDAOXML(TEST_USER_FILE);
			
			
			String userName = "testUser";
			String password = "stillUnhashedPassword";
			boolean admin = false;

			assertNull(userDAO.retrieveUser(userName));
			
			User newUser = new User(userName, password, admin);
			userDAO.addUser(newUser);
			
			assertNotNull(userDAO.retrieveUser(userName));
			
		} catch (BookstoreException e) {
			fail("The test should not end up here!");
		}
	}

	@Test
	void testRetrieveUser() {
		try {
			UserDAO userDAO = new UserDAOXML(TEST_USER_FILE);
			
			String userName = "administer";
			String password = "hardPasswordStillPlainText";
			boolean admin = true;
			User newUser = new User(userName, password, admin);

			userDAO.addUser(newUser);
			
			User administer = userDAO.retrieveUser(userName);
			assertTrue(newUser.equals(administer));
			
//			Maybe the saving/loading the same file could mess up the characters
			userName = "user hårdçäöàèã";
			password = "hardPasswordStillPlainText";
			admin = false;
			User userHardName = new User(userName, password, admin);

			userDAO.addUser(userHardName);
			
			User retrievedHardNameUser = userDAO.retrieveUser(userName);
			assertEquals(userName, retrievedHardNameUser.getUsername());
			
		} catch (BookstoreException e) {
			fail("The test should not end up here!");
		}
	}

	@Test
	void testRemoveUser() {
		try {
			UserDAO userDAO = new UserDAOXML(TEST_USER_FILE);
			
			String userName = "testUser";
			User user = userDAO.retrieveUser(userName);
			assertNotNull(user);
			assertEquals(userName, user.getUsername());
			
			userDAO.removeUser(user);
			
			User removedUser = userDAO.retrieveUser(userName);
			assertNull(removedUser);
			
			
		} catch (BookstoreException e) {
			fail("The test should not end up here!");
		}
		
	}
	
	@AfterAll
	static void cleanUserDatabase() throws Exception {
		File testFile = new File(TEST_USER_FILE);
		testFile.delete();
	}

}
