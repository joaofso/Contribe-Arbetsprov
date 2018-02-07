package io.github.joaofso.bookstore.control;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.model.Book;

public class BookStore implements BookList {
	
	private static final String WRONG_QUANTITY = "The amount of books to be added is invalid";
	
	private BookController bookController;
	private UserController userController;
	
	public BookStore() throws BookstoreException {
		this.bookController = new BookController();
		this.userController = new UserController();
	}

	/**
	 * {@inheritDoc}
	 */
	public Book[] list(String searchString) {
		if(searchString.equals("")) {
			Set<Book> listBooks = new HashSet<Book>(this.bookController.listAll());
			Book[] books = new Book[listBooks.size()];
			return listBooks.toArray(books);
		}else {
			Set<Book> returnedBooks = new HashSet<Book>();
			Set<String> uniqueStrings = new HashSet<String>(Arrays.asList(searchString.split(" ")));
			for (String token : uniqueStrings) {
				returnedBooks.addAll(this.bookController.searchByAuthor(token));
				returnedBooks.addAll(this.bookController.searchByTitle(token));
			}
			Book[] booksArray = new Book[returnedBooks.size()];
			return returnedBooks.toArray(booksArray);
		}
	}

	@Override
	public boolean add(Book book, int quantity) throws BookstoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] buy(Book... books) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	private void validateQuantity(int quantity) throws BookstoreException {
		if(quantity < 1) {
			throw new BookstoreException(WRONG_QUANTITY);
		}
	}

}
