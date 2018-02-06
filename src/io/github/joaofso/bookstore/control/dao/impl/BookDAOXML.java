package io.github.joaofso.bookstore.control.dao.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.control.dao.BookDAO;
import io.github.joaofso.bookstore.model.Book;

/**
 * This class implements the DAO pattern, in order to encapsulate the logic of
 * the access to the actual storage from the rest of the application. It stores
 * the data regarding the books of the bookstore in an XML file, since our
 * exercise is a small system; however, to use a database or even a tableless
 * system, we just need to plug the correspondent DAO implementation.
 * 
 * @author João Felipe
 *
 */
public class BookDAOXML implements BookDAO {

	private final String DEFAULT_BOOK_FILE = "./BookDatabase.xml";
	private final String IT_IS_NOT_POSSIBLE_CREATE_FILE = "It is not possible to create the Book storage file in: ";
	private final String INVALID_XML_FILE = "The XML file with the book database is invalid";

	private File bookstoreDBFile = null;

	/**
	 * Constructor of the DAO object. It receives a filepath to be the book
	 * database.
	 * 
	 * @param filepath The file path to the xml file that contain the books of the
	 *        bookstore.
	 * @throws BookstoreException Thrown when the system cannot access the file path due to, for
	 *         instance lack of permission.
	 */
	public BookDAOXML(String filepath) throws BookstoreException {
		bookstoreDBFile = new File(filepath);
		try {
			if (!bookstoreDBFile.exists()) {
				bookstoreDBFile.createNewFile();
			}
		} catch (IOException e) {
			throw new BookstoreException(IT_IS_NOT_POSSIBLE_CREATE_FILE + filepath);
		}
	}

	/**
	 * Constructor of the DAO object. It uses a default file path to manage the
	 * books data base.
	 * 
	 * @throws BookstoreException
	 *             Thrown when the system cannot access the file path due to, for
	 *             instance lack of permission.
	 */
	public BookDAOXML() throws BookstoreException {
		new BookDAOXML(DEFAULT_BOOK_FILE);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Book> listAll() {
		List<Book> allBooks = new LinkedList<Book>();
		try {
			Document dbDoc = this.parseFile();
			List<Element> books = dbDoc.getRootElement().getChildren("book");
			for (Element book : books) {
				allBooks.add(this.getBook(book));
			}
		} catch (BookstoreException e) {
			return null;
		}
		return allBooks;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Book> searchByTitle(String title) {
		List<Book> booksByTitle = new LinkedList<Book>();
		try {
			Document dbDoc = this.parseFile();
			List<Element> books = dbDoc.getRootElement().getChildren("book");
			for (Element book : books) {
				if (book.getChildText("title").contains(title)) {
					booksByTitle.add(this.getBook(book));
				}
			}
		} catch (BookstoreException e) {
			return null;
		}
		return booksByTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Book> searchByAuthor(String authorName) {
		List<Book> booksByAuthor = new LinkedList<Book>();
		try {
			Document dbDoc = this.parseFile();
			List<Element> books = dbDoc.getRootElement().getChildren("book");
			for (Element book : books) {
				if (book.getChildText("author").contains(authorName)) {
					booksByAuthor.add(this.getBook(book));
				}
			}
		} catch (BookstoreException e) {
			return null;
		}
		return booksByAuthor;
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public boolean insertBook(Book book) {
		try {
			Document dbDoc = this.parseFile();
			if (!dbDoc.hasRootElement()) {
				dbDoc.setRootElement(new Element("bookstore"));
			}
			Element newBook = this.getXML(book);
			dbDoc.getRootElement().addContent(newBook);

			this.saveFile(dbDoc);
			return true;
		} catch (BookstoreException e) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean deleteBook(Book book) {
		try {
			Document dbDoc = this.parseFile();
			List<Element> books = dbDoc.getRootElement().getChildren("book");
			for (Element storedBook : books) {
				if (storedBook.getChildText("author").equals(book.getAuthor())
						&& storedBook.getChildText("title").equals(book.getTitle())
						&& storedBook.getChildText("price").equals(book.getPrice().toString())) {

					storedBook.getParent().removeContent(storedBook);
					this.saveFile(dbDoc);
					return true;
				}
			}
		} catch (BookstoreException e) {
		}
		return false;
	}

	private Document parseFile() throws BookstoreException {
		if (this.bookstoreDBFile.length() == 0) {
			return new Document();
		}
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(this.bookstoreDBFile);
		} catch (JDOMException | IOException e) {
			throw new BookstoreException(INVALID_XML_FILE);
		}
		return doc;
	}

	synchronized private void saveFile(Document dbDoc) throws BookstoreException {
		try {
			XMLOutputter outputter = new XMLOutputter();
			FileWriter writer = new FileWriter(this.bookstoreDBFile);
			outputter.output(dbDoc, writer);
			writer.close();
		} catch (IOException e) {
			throw new BookstoreException(INVALID_XML_FILE);
		}

	}

	private Book getBook(Element element) {
		return new Book(element.getChildText("title"), element.getChildText("author"),
				new BigDecimal(element.getChildText("price")));
	}

	private Element getXML(Book book) {
		Element bookTag = new Element("book");
		bookTag.addContent(new Element("title").setText(book.getTitle()));
		bookTag.addContent(new Element("author").setText(book.getAuthor()));
		bookTag.addContent(new Element("price").setText(book.getPrice().toString()));

		return bookTag;
	}

}
