package io.github.joaofso.bookstore.control;

import java.math.BigDecimal;
import java.util.List;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.control.dao.BookDAO;
import io.github.joaofso.bookstore.control.dao.impl.BookDAOXML;
import io.github.joaofso.bookstore.model.Book;

/**
 * This class controls the BookDAO to manage the activities related to books.
 * Furthermore, it separates the model layer from the view layer. It has the
 * capabilities to provide the operations over the books, inserting the security
 * layer, abstracting the storage mechanism.
 * 
 * @author João Felipe
 *
 */
public class BookController{
	
	private BookDAO bookDAO;
	
	/**
	 * Constructor of a user controller.
	 * @throws BookstoreException
	 */
	public BookController() throws BookstoreException {
		this.bookDAO = new BookDAOXML();
	}

	/**
	 * Lists all the books registered in the book store.
	 * @return A list with the registered books.
	 */
	public List<Book> listAll() {
		return this.bookDAO.listAll();
	}
	
	/**
	 * Search books by author. Here we add a layer of verification to the operation.
	 * @param author The author name to be searched.
	 * @return A list with the books published by the referred author.
	 */
	public List<Book> searchByAuthor(String author) {
		this.validateAuthorName(author);
		return this.bookDAO.searchByAuthor(author);
	}
	
	/**
	 * Search books by title. Here we add a layer of verification to the operation.
	 * @param title The title of the book.
	 * @return A list with the books with the referred title.
	 */
	public List<Book> searchByTitle(String title) {
		this.validateBookTitle(title);
		return this.bookDAO.searchByTitle(title);
	}
	
	/**
	 * Add a new book in the system
	 * @throws BookstoreException 
	 */
	public boolean add(String title, String author, String price) throws BookstoreException {
		this.validateBookTitle(title);
		this.validateAuthorName(author);
		this.validatePrice(price);
		BigDecimal formattedPrice = this.formatPrice(price);

		Book newBook = new Book(title, author, formattedPrice);
		boolean result = this.bookDAO.insertBook(newBook);
		
		return result;
	}
	
	/**
	 * Remove a book from the system. 
	 * @param book The book to be removed.
	 * @return a boolean value indicating whether the book was removed correctly.
	 */
	public boolean remove(Book book) {
		return this.bookDAO.deleteBook(book);
	}

	
	private void validateBookTitle(String title) {
//		Add any rule for malformed title names
	}

	private void validateAuthorName(String author) {
//		Add any rule for malformed author names
	}

	private void validatePrice(String price) {
//		Add any other rule for incompatible price format
		
	}

	private BigDecimal formatPrice(String price) {
		String newPrice = price.replaceAll(",", "").trim();
		return new BigDecimal(newPrice);
	}
}
