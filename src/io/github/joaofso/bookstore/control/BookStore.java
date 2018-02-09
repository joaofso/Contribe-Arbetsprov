package io.github.joaofso.bookstore.control;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.aux.PurchaseStatus;
import io.github.joaofso.bookstore.model.Book;
import io.github.joaofso.bookstore.model.ShoppingBasket;
import io.github.joaofso.bookstore.model.User;

/**
 * This class is the system's facade. It keeps track of the user and shopping
 * basket, using both user and book controllers to perform its activities.
 * 
 * @author João Felipe
 *
 */
public class BookStore implements BookList {

	private static final String WRONG_QUANTITY = "The amount of books to be added is invalid";

	private BookController bookController;
	private UserController userController;

	private ShoppingBasket shoppingBasket;
	private User loggedUser;

	public BookStore() throws BookstoreException {
		this.bookController = new BookController();
		this.userController = new UserController();

		this.shoppingBasket = null;
		this.loggedUser = null;
	}

	/**
	 * Logs in an user and creates a shopping basket for this user.
	 * 
	 * @param username
	 *            The username for the user.
	 * @param password
	 *            The password to be matched with the one from the database.
	 * @throws BookstoreException
	 *             Thrown when the provided username and password do not match.
	 */
	public void loginUser(String username, String password) throws BookstoreException {
		this.loggedUser = this.userController.loginUser(username, password);
		this.shoppingBasket = new ShoppingBasket();
	}

	/**
	 * {@inheritDoc}
	 */
	public Book[] list(String searchString) {
		// The user is not logged in yet
		if (this.loggedUser == null || this.shoppingBasket == null) {
			return new Book[0];
		}

		if (searchString.equals("")) {
			List<Book> listBooks = this.getUnique(new LinkedList<Book>(this.bookController.listAll()));
			Book[] books = new Book[listBooks.size()];
			return listBooks.toArray(books);
		} else {
			List<Book> returnedBooks = new LinkedList<Book>();
			Set<String> uniqueStrings = new TreeSet<String>(Arrays.asList(searchString.split(" ")));
			for (String token : uniqueStrings) {
				List<Book> byAuthor = this.bookController.searchByAuthor(token);
				for (Book book : byAuthor) {
					if(!returnedBooks.contains(book)) {
						returnedBooks.add(book);
					}
				}
				
				List<Book> byTitle = this.bookController.searchByTitle(token);
				for (Book book : byTitle) {
					if(!returnedBooks.contains(book)) {
						returnedBooks.add(book);
					}
				}
			}
			Book[] booksArray = new Book[returnedBooks.size()];
			return returnedBooks.toArray(booksArray);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean add(Book book, int quantity) throws BookstoreException {
		if (this.loggedUser == null || this.shoppingBasket == null) {
			return false;
		}
		int i = 0;
		for (i = 0; i < quantity; i++) {
			this.shoppingBasket.addItem(book);
		}
		return true;
	}

	/**
	 * Remove books from the shopping basket.
	 * 
	 * @param book
	 *            The book to be removed.
	 * @param quantity
	 *            The number of exemplars of this book to be removed from the
	 *            basket.
	 * @return A boolean value indicating whether the operation was performed
	 *         correctly.
	 * @throws BookstoreException
	 */
	public boolean remove(Book book, int quantity) throws BookstoreException {
		if (this.loggedUser == null || this.shoppingBasket == null) {
			return false;
		}
		int i = 0;
		for (i = 0; i < quantity; i++) {
			this.shoppingBasket.removeItem(book);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public int[] buy(Book... books) {
		if (this.loggedUser == null || this.shoppingBasket == null) {
			return new int[0];
		}

		this.processPayment();

		int[] purchaseStatuses = new int[books.length];
		for (int i = 0; i < books.length; i++) {

			boolean removed = this.bookController.remove(books[i]);
			PurchaseStatus purchResult = removed ? PurchaseStatus.OK : PurchaseStatus.NOT_IN_STOCK;
			purchaseStatuses[i] = purchResult.getStatus();
		}

		// Cleaning the basket after the purchase
		this.shoppingBasket = new ShoppingBasket();
		return purchaseStatuses;
	}
	
	public ShoppingBasket getShoppingBasket() {
		return this.shoppingBasket;
	}

	// ============================================ADMIN TASKS====================================================

	public boolean addNewUser(String username, String password, boolean admin) {
		try {
			if(!admin || (admin && this.loggedUser != null && this.loggedUser.isAdmin()) ) {
				this.userController.createUser(username, password, admin);
				return true;
			}
			return false;
			
		} catch (BookstoreException e) {
			return false;
		}
	}

	public boolean removeUser(String username, String password) {

		if (this.loggedUser.getUsername().equals(username)) {
			// the user wants to remove its own account from the bookstore
			try {
				this.userController.loginUser(username, password);
				return this.userController.deteleUser(username, password);
			} catch (BookstoreException e) {
				return false;
			}
		} else {
			// an admin wants to delete another account
			if (this.loggedUser.isAdmin()) {
				try {
					this.userController.loginUser(this.loggedUser.getUsername(), password);
					User userToBeRemoved = this.userController.retrieveUser(username);
					return this.userController.deteleUser(username, userToBeRemoved.getPassword());
				} catch (BookstoreException e) {
					return false;
				}
			}
			return false;
		}
	}

	public boolean addNewBook(String title, String author, String price) {
		try {
			return this.bookController.add(title, author, price);
		} catch (BookstoreException e) {
			return false;
		}
	}
	
	private void processPayment() {
		// I did not implement this method because it is not part of the activity
	}
	
	private List<Book> getUnique(List<Book> listOfBook){
		List<Book> result = new LinkedList<Book>();
		for (Book book : listOfBook) {
			if(!result.contains(book)) {
				result.add(book);
			}
		}
		return result;
	}

}
