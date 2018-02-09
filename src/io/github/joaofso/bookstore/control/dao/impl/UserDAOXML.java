package io.github.joaofso.bookstore.control.dao.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import io.github.joaofso.bookstore.aux.BookstoreException;
import io.github.joaofso.bookstore.control.dao.UserDAO;
import io.github.joaofso.bookstore.model.User;

/**
 * This class implements the DAO pattern, in order to encapsulate the logic of
 * the access to the actual storage from the rest of the application. It stores
 * the data regarding the users of the bookstore in an XML file, since our
 * exercise is a small system; however, to use a database or even a tableless
 * system, we just need to plug the correspondent DAO implementation.
 * 
 * @author João Felipe
 *
 */
public class UserDAOXML implements UserDAO {

	private final static String DEFAULT_USER_FILE = "./UserDatabase.xml";

	private final static String IT_IS_NOT_POSSIBLE_CREATE_FILE = "It is not possible to create the User storage file in: ";
	private final static String INVALID_XML_FILE = "The XML file with the user database is invalid";

	private File userDBFile;

	/**
	 * Constructor of the DAO object. It receives a filepath to be the book
	 * database.
	 * 
	 * @param filepath The file path to the xml file that contain the users of the
	 *        bookstore.
	 * @throws BookstoreException Thrown when the system cannot access the file path due to, for
	 *         instance lack of permission.
	 */
	public UserDAOXML(String filepath) throws BookstoreException {
		this.userDBFile = new File(filepath);
		try {
			if (!this.userDBFile.exists()) {
				this.userDBFile.createNewFile();

				Document dbDoc = new Document(new Element("users"));
				this.saveFile(dbDoc);
			}
		} catch (IOException e) {
			throw new BookstoreException(IT_IS_NOT_POSSIBLE_CREATE_FILE + filepath);
		}
	}

	/**
	 * Constructor of the DAO object. It uses a default file path to manage the
	 * users data base.
	 * 
	 * @throws BookstoreException
	 *             Thrown when the system cannot access the file path due to, for
	 *             instance lack of permission.
	 */
	public UserDAOXML() throws BookstoreException {
		this(DEFAULT_USER_FILE);
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public boolean addUser(User newUser) {
		try {
			if(this.retrieveUser(newUser.getUsername()) != null) {
				return false;
			}
			Element userxml = this.getXML(newUser);
			
			Document dbDoc = this.parseFile();
			dbDoc.getRootElement().addContent(userxml);

			this.saveFile(dbDoc);
			return true;
		} catch (BookstoreException e) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public User retrieveUser(String username) {
		try {
			Document dbDoc = this.parseFile();
			List<Element> users = dbDoc.getRootElement().getChildren("user");
			for (Element user : users) {
				if(user.getChildText("username").equals(username)){
					return this.getUser(user);
				}
			}
		} catch (BookstoreException e) {
			return null;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean removeUser(User user) {
		try {
			Document dbDoc = this.parseFile();
			List<Element> users = dbDoc.getRootElement().getChildren("user");
			for (Element storedUser : users) {
				if (storedUser.getChildText("username").equals(user.getUsername())
						&& storedUser.getChildText("password").equals(user.getPassword())
						&& storedUser.getChildText("admin").equals(Boolean.toString(user.isAdmin()))) {

					storedUser.getParent().removeContent(storedUser);
					this.saveFile(dbDoc);
					return true;
				}
			}
		} catch (BookstoreException e) {
		}
		return false;
	}
	
	
	private Document parseFile() throws BookstoreException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(this.userDBFile);
		} catch (JDOMException | IOException e) {
			throw new BookstoreException(INVALID_XML_FILE);
		}
		return doc;
	}
	
	synchronized private void saveFile(Document dbDoc) throws BookstoreException {
		try {
			XMLOutputter outputter = new XMLOutputter();
			FileWriter writer = new FileWriter(this.userDBFile);
			outputter.output(dbDoc, writer);
			writer.close();
		} catch (IOException e) {
			throw new BookstoreException(INVALID_XML_FILE);
		}

	}
	
	private User getUser(Element element) {
		return new User(element.getChildText("username"), element.getChildText("password"), Boolean.valueOf(element.getChildText("admin")));
	}

	private Element getXML(User user) {
		Element userTag = new Element("user");
		userTag.addContent(new Element("username").setText(user.getUsername()));
		userTag.addContent(new Element("password").setText(user.getPassword()));
		userTag.addContent(new Element("admin").setText(Boolean.toString(user.isAdmin())));

		return userTag;
	}

}
