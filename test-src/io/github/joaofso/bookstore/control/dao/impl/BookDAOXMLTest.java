package io.github.joaofso.bookstore.control.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.control.dao.BookDAO;
import io.github.joaofso.bookstore.model.Book;

public class BookDAOXMLTest {

	private final static String TEST_BOOK_FILE = "./TEST_BookDatabase.xml";
	private final static String INITIAL_FILE_SET = "./ContribeExerciseData.txt";
	
	
	@BeforeEach
	void populateBooksDatabase() {
		try {
			BookDAO bookDAO = new BookDAOXML(TEST_BOOK_FILE);
			
			Scanner scanner = new Scanner(new File(INITIAL_FILE_SET));
			
			while (scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(";");
				String title = tokens[0];
				String author = tokens[1];
				String priceString = tokens[2].replaceAll(",", "");
				BigDecimal price = new BigDecimal(priceString);
				int quantity = Integer.valueOf(tokens[3]);
				
				for (int i = 0; i < quantity; i++) {
					Book newBook = new Book(title, author, price);
					bookDAO.insertBook(newBook);
				}
			}
			scanner.close();
		} catch (BookstoreException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Test
	void testListAll() {
		try {
			BookDAO bookDAO = new BookDAOXML(TEST_BOOK_FILE);
			List<Book> allBooks = bookDAO.listAll();
			
			assertEquals(47, allBooks.size());
		} catch (BookstoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testSearchByTitle() {
		try {
			BookDAO bookDAO = new BookDAOXML(TEST_BOOK_FILE);
			
			assertEquals(0, bookDAO.searchByTitle("Inexistent Title").size());
			
			//The values tested here come from the example file provided in the exercise page
			assertEquals(15, bookDAO.searchByTitle("Mastering åäö").size());
			assertEquals(1, bookDAO.searchByTitle("How To Spend Money").size());
			assertEquals(8, bookDAO.searchByTitle("Generic Title").size());
			assertEquals(23, bookDAO.searchByTitle("Random Sales").size());
			assertEquals(0, bookDAO.searchByTitle("Desired").size());
			
			
		} catch (BookstoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testSearchByAuthor() {
		try {
			BookDAO bookDAO = new BookDAOXML(TEST_BOOK_FILE);
			
			assertEquals(0, bookDAO.searchByAuthor("Inexistent author").size());
			
			//The values tested here come from the example file provided in the exercise page
			assertEquals(15, bookDAO.searchByAuthor("Average Swede").size());
			assertEquals(1, bookDAO.searchByAuthor("Rich Bloke").size());
			assertEquals(5, bookDAO.searchByAuthor("First Author").size());
			assertEquals(3, bookDAO.searchByAuthor("Second Author").size());
			assertEquals(23, bookDAO.searchByAuthor("Cunning Bastard").size());
			
			
		} catch (BookstoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testInsertBook() {
		try {
			BookDAO bookDAO = new BookDAOXML(TEST_BOOK_FILE);
			
			assertEquals(47, bookDAO.listAll().size());
			
			Book newBook = new Book("A New Bök", "Rich Bloke", new BigDecimal("10.50"));
			bookDAO.insertBook(newBook);
			
			assertEquals(48, bookDAO.listAll().size());
			assertEquals(2, bookDAO.searchByAuthor("Rich Bloke").size());
			
			Book secondNewBook = new Book("Desired", "New Author", new BigDecimal("20"));
			bookDAO.insertBook(secondNewBook);
			
			assertEquals(49, bookDAO.listAll().size());
			assertEquals(1, bookDAO.searchByTitle("Desired").size());
			
		} catch (BookstoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testDeleteBook() {
		try {
			BookDAO bookDAO = new BookDAOXML(TEST_BOOK_FILE);
			
			assertEquals(47, bookDAO.listAll().size());
			
			//There is just one book of this author
			Book toRemove = bookDAO.searchByAuthor("Rich Bloke").get(0);
			bookDAO.deleteBook(toRemove);
			
			assertEquals(46, bookDAO.listAll().size());
			assertEquals(0, bookDAO.searchByAuthor("Rich Bloke").size());
			
			Book secondToRemove = bookDAO.searchByTitle("Random Sales").get(0);
			bookDAO.deleteBook(secondToRemove);
			
			assertEquals(45, bookDAO.listAll().size());
			assertEquals(22, bookDAO.searchByTitle("Random Sales").size());
			
		} catch (BookstoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterEach
	void cleanBooksDatabase() throws Exception {
		File testFile = new File(TEST_BOOK_FILE);
		testFile.delete();
	}

}
