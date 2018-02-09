package io.github.joaofso.bookstore.control;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.aux.PurchaseStatus;
import io.github.joaofso.bookstore.model.Book;
import io.github.joaofso.bookstore.model.ShoppingBasket;
import io.github.joaofso.bookstore.model.ShoppingBasketItem;

public class BookStoreTest {
	
	private final static String INITIAL_FILE_SET = "./ContribeExerciseData.txt";
	
	private final static String DEFAULT_USER_FILE = "./UserDatabase.xml";
	private final static String DEFAULT_BOOK_FILE = "./BookDatabase.xml";

	@BeforeEach
	void startTesting() {
		File userDbFile = new File(DEFAULT_USER_FILE);
		if(userDbFile.exists()) {
			userDbFile.delete();
		}
		File bookDbFile = new File(DEFAULT_BOOK_FILE);
		if(bookDbFile.exists()) {
			bookDbFile.delete();
		}
		
		// I use this method to create the admin because it is not possible to add the
		// first administer through the system facade.
		try {
			String adminUsername = "adm";
			String adminPassword = "admpass";
			UserController control = new UserController();
			control.createUser(adminUsername, adminPassword, true);
		} catch (BookstoreException e) {
		}
	}
	
	@Test
	void testAddNewUserSuccess() {
		try {
			BookStore store = new BookStore();
			
			boolean userResult = store.addNewUser("first user", "passwordfirst", false);
			assertEquals(true, userResult);
			store.loginUser("first user", "passwordfirst");
			
		} catch (BookstoreException e) {
			fail("Should not reach this line!");
		}
	}
	
	@Test
	void testAddNewUserFailure() {
		try {
			BookStore store = new BookStore();
			
//			just a logged in administer is able to add an administer
			boolean user1Result = store.addNewUser("second user", "passwordfirst", true);
			assertEquals(false, user1Result);
			
		} catch (BookstoreException e) {
			fail("Should not reach this line!");
		}
	}

	@Test
	void testRemoveUserSuccess() {
		try {
			BookStore store = new BookStore();
			
//			adding a regular user, logging in and the same user removes its account
			String username = "regularuser";
			String password = "regularpassword";
			boolean addResult = store.addNewUser(username, password, false);
			assertTrue(addResult);
			store.loginUser(username, password);
			
			boolean removeResult = store.removeUser(username, password);
			assertTrue(removeResult);
			
			
//			adding a new administer
			username = "adm";
			password = "admpass";
			store.loginUser(username, password);
			
			boolean addAdmin2Result = store.addNewUser("secondadmin", "adm2pass", true);
			assertTrue(addAdmin2Result);
			
			boolean removeAdmin2Result = store.removeUser(username, password);
			assertTrue(removeAdmin2Result);
			
		} catch (BookstoreException e) {
			fail("Should not reach this line!");
		}
	}
	
	@Test
	void testRemoveUserFail() {
		try {
			BookStore store = new BookStore();
			
//			adding two regular users, logging in with one it fails to remove the other
			String username = "regularuser";
			String password = "regularpassword";
			store.addNewUser(username, password, false);
			store.loginUser(username, password);
			
			username = "regularuser2";
			password = "regularpassword2";
			store.addNewUser(username, password, false);
			
			boolean removeResult = store.removeUser(username, password);
			assertFalse(removeResult);
			
			
//			adding a new administer
			username = "adm";
			password = "admpass";
			store.loginUser(username, password);
			
			store.addNewUser("secondadmin", "adm2pass", true);
			
			username = "regularuser";
			password = "regularpassword";
			store.loginUser(username, password);
			
//			a regular user is not able to remove an admin from the system
			boolean removeAdminResult = store.removeUser("adm", password);
			assertFalse(removeAdminResult);
			
		} catch (BookstoreException e) {
			fail("Should not reach this line!");
		}
	}

	@Test
	void testSuccessfullLoginUser() {
		try {
			String username = "adm";
			String password = "admpass";
			BookStore store = new BookStore();
			store.loginUser(username, password);
			assertTrue(true);
		} catch (BookstoreException e) {
			fail("Should not reach this line!");
		}
		
		try {
//			this user is already in the database, added in a previous test
			String username = "first user";
			String password = "passwordfirst";
			BookStore store = new BookStore();
			store.addNewUser(username, password, false);
			store.loginUser(username, password);
			assertTrue(true);
		} catch (BookstoreException e) {
			fail("Should not reach this line!");
		}
	}
	
	@Test
	void testFailureLoginUser() {
		try {
//		this user is already in the database, but providing a wrong password
			String username = "adm";
			String password = "admwrongpassword";
			BookStore store = new BookStore();
			store.loginUser(username, password);
			fail("It should throw an exception");
			
		} catch (BookstoreException e) {
			assertTrue(true);
		}
		
		try {
//			this user is not in the database
			String username = "unknown user";
			String password = "unknownpassword";
			BookStore store = new BookStore();
			store.loginUser(username, password);
			fail("It should throw an exception");
		} catch (BookstoreException e) {
			assertTrue(true);
		}
	}

	@Test
	void testList() {
		try {
			BookStore store = new BookStore();
			Book[] bookList = store.list("");
			
			assertEquals(0, bookList.length);
			
			populateBooksDatabase();
			
//			the system still shows 0 books because there is no logged user
			bookList = store.list("");
			assertEquals(0, bookList.length);
			
			String username = "adm";
			String password = "admpass";
			store.loginUser(username, password);
			
//			the books from the provided file. Here, the system just shows the unique entries from the database
			bookList = store.list("");
			assertEquals(6, bookList.length);
			
			bookList = store.list("Mastering åäö Average Swede");
			assertEquals(1, bookList.length);
//			just reversing the keywords
			bookList = store.list("Average Swede Mastering åäö");
			assertEquals(1, bookList.length);
			
			bookList = store.list("Bastard");
			assertEquals(2, bookList.length);
			
		} catch (BookstoreException e) {
			fail("It should not reach this line!");
		}
	}

	@Test
	void testAddNewBook() {
		try {
			BookStore store = new BookStore();
			
			populateBooksDatabase();
			
//			initial state of the library
			String username = "adm";
			String password = "admpass";
			store.loginUser(username, password);
			
			Book[] bookList = store.list("");
			assertEquals(6, bookList.length);
			
//			adding a book that already exists in the library
			store.addNewBook("Generic Title", "First Author", "185.50");
			bookList = store.list("");
			assertEquals(6, bookList.length);
			
//			adding a book from the existent author
			bookList = store.list("First author");
			assertEquals(1, bookList.length);
			
			store.addNewBook("New Title", "First Author", "12.70");

			bookList = store.list("First author");
			assertEquals(2, bookList.length);

			bookList = store.list("");
			assertEquals(7, bookList.length);
			
//			adding a book with a title already present in the system, but from other author
			bookList = store.list("How To Spend Money");
			assertEquals(1, bookList.length);
			
			store.addNewBook("How To Spend Money", "RichAuthor", "99999.99");

			bookList = store.list("How To Spend Money");
			assertEquals(2, bookList.length);
			
			bookList = store.list("RichAuthor");
			assertEquals(1, bookList.length);

			bookList = store.list("");
			assertEquals(8, bookList.length);
			
		} catch (BookstoreException e) {
			fail("It should not reach this line!");
		}
	}
	
	@Test
	void testAddBasketBook() {

	try {
		BookStore store = new BookStore();
//		there is no logged in user
		assertNull(store.getShoppingBasket());
		
		populateBooksDatabase();
		
		String username = "adm";
		String password = "admpass";
		store.loginUser(username, password);
		
		ShoppingBasket basket = store.getShoppingBasket();
		assertEquals(BigDecimal.valueOf(0), basket.getTotal());
		assertEquals(0, basket.getItems().size());
		
//		adding a book to the shopping basket
		Book bookToAddInBasket = store.list("Mastering åäö Average Swede")[0];
		store.add(bookToAddInBasket, 1);
		BigDecimal expected = BigDecimal.valueOf(762.0);
		BigDecimal total = basket.getTotal();
		assertTrue(expected.compareTo(total) == 0);
		assertEquals(1, basket.getItems().size());
		
//		adding a book that just has one unit in the database
		bookToAddInBasket = store.list("How To Spend Money Rich Bloke")[0];
		store.add(bookToAddInBasket, 1);
		assertTrue(BigDecimal.valueOf(1000762).compareTo(basket.getTotal()) == 0);
		assertEquals(2, basket.getItems().size());
		
	} catch (BookstoreException e) {
		fail("It should not reach this line!");
	}
		
	}
	
	@Test
	void testRemoveBasketBook() {
		try {
			BookStore store = new BookStore();
			
			populateBooksDatabase();
			
			String username = "adm";
			String password = "admpass";
			store.loginUser(username, password);
			
			Book bookToAddInBasket = store.list("Mastering åäö Average Swede")[0];
			store.add(bookToAddInBasket, 2);
			bookToAddInBasket = store.list("How To Spend Money Rich Bloke")[0];
			store.add(bookToAddInBasket, 1);
			assertTrue(BigDecimal.valueOf(1001524).compareTo(store.getShoppingBasket().getTotal()) == 0);
			assertEquals(3, store.getShoppingBasket().size());
			
//			removing a book from the shopping basket, but still there is one copy of it there
			Book bookToRemove = store.getShoppingBasket().getItems().get(0).getItem();
			store.remove(bookToRemove, 1);
			assertTrue(BigDecimal.valueOf(1000762).compareTo(store.getShoppingBasket().getTotal()) == 0);
			assertEquals(2, store.getShoppingBasket().size());
			
//			removing the expensive book from the shopping basket, but still there is one book there
			bookToRemove = store.getShoppingBasket().getItems().get(1).getItem();
			store.remove(bookToRemove, 1);
			assertTrue(BigDecimal.valueOf(762).compareTo(store.getShoppingBasket().getTotal()) == 0);
			assertEquals(1, store.getShoppingBasket().size());
			
//			removing the last book from the shopping basket
			bookToRemove = store.getShoppingBasket().getItems().get(0).getItem();
			store.remove(bookToRemove, 1);
			assertTrue(BigDecimal.valueOf(0).compareTo(store.getShoppingBasket().getTotal()) == 0);
			assertEquals(0, store.getShoppingBasket().size());
			
			
		} catch (BookstoreException e) {
			fail("It should not reach this line!");
		}
	}

	@Test
	void testBuySuccess() {
		try {
			BookStore store = new BookStore();
			
			populateBooksDatabase();
			
			String username = "adm";
			String password = "admpass";
			store.loginUser(username, password);
			
			Book bookToAddInBasket = store.list("Mastering åäö Average Swede")[0];
			store.add(bookToAddInBasket, 2);
			bookToAddInBasket = store.list("How To Spend Money Rich Bloke")[0];
			store.add(bookToAddInBasket, 1);

			List<Book> booksToBuy = new LinkedList<Book>();
			ShoppingBasket basket = store.getShoppingBasket();
			for (ShoppingBasketItem item : basket.getItems()) {
				for (int i = 0; i < item.getQuantity(); i++) {
					booksToBuy.add(item.getItem());
				}
			}
			Book[] books = new Book[booksToBuy.size()];
			int[] purchaseResults = store.buy(booksToBuy.toArray(books));
			for (int i : purchaseResults) {
				assertEquals(PurchaseStatus.OK.getStatus(), i);
			}
			
			assertEquals(0, store.list("Rich Bloke").length);
			assertEquals(5, store.list("").length);
			
		} catch (BookstoreException e) {
			fail("It should not reach this line!");
		}
	}
	
	@Test
	void testBuyFail() {
		try {
			BookStore store = new BookStore();
			
			populateBooksDatabase();
			
			String username = "adm";
			String password = "admpass";
			store.loginUser(username, password);
			
			Book bookToAddInBasket = store.list("How To Spend Money Rich Bloke")[0];
			store.add(bookToAddInBasket, 2);

			List<Book> booksToBuy = new LinkedList<Book>();
			ShoppingBasket basket = store.getShoppingBasket();
			for (ShoppingBasketItem item : basket.getItems()) {
				for (int i = 0; i < item.getQuantity(); i++) {
					booksToBuy.add(item.getItem());
				}
			}
			Book[] books = new Book[booksToBuy.size()];
			int[] purchaseResults = store.buy(booksToBuy.toArray(books));
			assertEquals(PurchaseStatus.OK.getStatus(), purchaseResults[0]);
			assertEquals(PurchaseStatus.NOT_IN_STOCK.getStatus(), purchaseResults[1]);
			
			assertEquals(0, store.list("Rich Bloke").length);
			assertEquals(5, store.list("").length);
			
		} catch (BookstoreException e) {
			fail("It should not reach this line!");
		}
	}

	static void populateBooksDatabase() {
		try {
			BookStore store = new BookStore();
			
			Scanner scanner = new Scanner(new File(INITIAL_FILE_SET));
			
			while (scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(";");
				String title = tokens[0];
				String author = tokens[1];
				String price = tokens[2];
				int quantity = Integer.valueOf(tokens[3]);
				
				for (int i = 0; i < quantity; i++) {
					store.addNewBook(title, author, price);
				}
			}
			scanner.close();
		} catch (BookstoreException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@AfterAll
	static void cleanBooksDatabase() throws Exception {
		File userDbFile = new File(DEFAULT_USER_FILE);
		if(userDbFile.exists()) {
			userDbFile.delete();
		}
		File bookDbFile = new File(DEFAULT_BOOK_FILE);
		if(bookDbFile.exists()) {
			bookDbFile.delete();
		}
	}

}
