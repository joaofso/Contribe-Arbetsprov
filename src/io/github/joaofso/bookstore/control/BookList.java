package io.github.joaofso.bookstore.control;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.model.Book;

/**
 * This interface defines the behavior of an object that manipulates books in
 * the system.
 * 
 * @author João Felipe
 *
 */
public interface BookList {
	
	/**
	 * Retrieves books based on string information.
	 * @param searchString The terms used to search books.
	 * @return The list of books that satisfy the provided search string.
	 */
	public Book[] list(String searchString);

	/**
	 * Adds books to the shopping cart.
	 * @param book The book to be added.
	 * @param quantity The number of units to be added.
	 * @return A boolean value indicating whether the book was added sucessfully.
	 * @throws BookstoreException 
	 */
	public boolean add(Book book, int quantity) throws BookstoreException;

	/**
	 * Actually performs the purchase.
	 * @param books The list of books to be bought.
	 * @return An array with the purchase status of each book.
	 */
	public int[] buy(Book... books);
}
