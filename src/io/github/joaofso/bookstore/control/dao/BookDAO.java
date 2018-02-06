package io.github.joaofso.bookstore.control.dao;

import java.util.List;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.model.Book;

/**
 * This interface defines the behavior of an object that manipulates directly
 * the books in the persistence layer.
 * 
 * @author João Felipe
 *
 */
public interface BookDAO {

	/**
	 * Lists every book stored currently in the system.
	 * @return A list with every book stored currently.
	 */
	List<Book> listAll();

	/**
	 * Searches the books, comparing with the provided title.
	 * @param title The title to be compared.
	 * @return A list of every book that match the titles with the provided title.
	 */
	List<Book> searchByTitle(String title);

	/**
	 * Searches the books, comparing with the provided author name.
	 * @param authorName The author name to be compared.
	 * @return A list of every book that match the provided author name.
	 * @throws BookstoreException 
	 */
	List<Book> searchByAuthor(String authorName);

	/**
	 * Adds a new book in the bookstore.
	 * @param book The book to be stored.
	 * @return A boolean value indicating that the book was really inserted.
	 * @throws BookstoreException 
	 */
	boolean insertBook(Book book);

	/**
	 * Removes a book from the bookstore
	 * @param book The book to be removed.
	 * @return A boolean value indicating that the book was really removed.
	 */
	boolean deleteBook(Book book);

}
