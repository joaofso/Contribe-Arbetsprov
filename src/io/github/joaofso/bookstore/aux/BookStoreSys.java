package io.github.joaofso.bookstore.aux;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import io.github.joaofso.bookstore.control.BookStore;
import io.github.joaofso.bookstore.model.Book;
import io.github.joaofso.bookstore.model.ShoppingBasket;
import io.github.joaofso.bookstore.model.ShoppingBasketItem;

public class BookStoreSys {
	
	private static final String TAB = "\t";

	public static void main(String[] arg) {
		
		try {
			BookStore bookStore = new BookStore();
			boolean loop = true;
			
			Book[] currentBookList = new Book[0];
			
			Scanner scanner = new Scanner(System.in);
			
			while(loop) {
				
				printMenu();
				System.out.println("Enter your command:");
				String line = scanner.nextLine();
				String[] tokens = line.split(" ");
				
				String command = tokens[0];
				
				switch (command) {
				
				case "login":
					if(tokens.length != 3) {
						printError("Wrong format for the login command. Please use: login [username] [password]");
					}
					String username = tokens[1];
					String password = tokens[2];
					
					bookStore.loginUser(username, password);
					
					printResult("Welcome to the BookStore, " + username + "!");
					
					break;
					
				case "list":
					String searchString = "";
					for (int i = 1; i < tokens.length; i++) {
						searchString.concat(tokens[i]);
					}
					
					currentBookList = bookStore.list(searchString);
					
					printBookList(currentBookList);
					
					break;
					
					
				case "add":
					if(tokens.length != 3) {
						printError("Wrong format for the add to the basket command. Please use: add [indexBook] [quantity]");
					}
					
					try {
						int bookIndex = Integer.parseInt(tokens[1]);
						
						if(bookIndex < 0) {
							printError("Wrong book index. Please do not use a negative number");
						}
						
						if(bookIndex >= currentBookList.length) {
							printError("Wrong book index. Please provide a value respecting the book list length");
						}
						
						int quantity = Integer.parseInt(tokens[2]);
						
						if(quantity < 0) {
							printError("Wrong book quantity. Please do not use a negative number");
						}
						
						Book book = currentBookList[bookIndex];
						boolean result = bookStore.add(book, quantity);
						
						if(result) {
							printResult("Book added successfully to your shopping basket!");
						}else {
							printError("The book could not be added to your basket");
						}
						
						printBasket(bookStore);
						
					}catch (NumberFormatException e) {
						printError("Wrong book index. Please provide an integer value");
					}
					
					break;
					
				case "remove":
					
					if(tokens.length != 3) {
						printError("Wrong format for the remove from the basket command. Please use: remove [indexBook] [quantity]");
					}
					
					try {
						int bookIndex = Integer.parseInt(tokens[1]);
						
						if(bookIndex < 0) {
							printError("Wrong book index. Please do not use a negative number");
						}
						
						if(bookIndex >= currentBookList.length) {
							printError("Wrong book index. Please provide a value respecting the basket list length. For more information");
						}
						
						ShoppingBasket basket = bookStore.getShoppingBasket();
						
						int quantity = Integer.parseInt(tokens[2]);
						
						if(quantity < 0) {
							printError("Wrong book quantity. Please do not use a negative number");
						}
						
						int currentQuantity = basket.getItems().get(bookIndex).getQuantity();
						
						if(quantity > currentQuantity) {
							printError("Wrong book quantity. Please provide a number lesser than " + currentQuantity + " for this book");
						}
						
						Book book = basket.getItems().get(bookIndex).getItem();
						boolean result = bookStore.remove(book, quantity);
						
						if(result) {
							printResult("Book removed successfully from your shopping basket!");
						}else {
							printError("The book could not be removed from your basket");
						}
						
						printBasket(bookStore);
						
					}catch (NumberFormatException e) {
						printError("Wrong book index. Please provide an integer value");
					}
					
					break;
					
				case "basket":
					if(tokens.length != 1) {
						printError("Wrong format for the showing your shopping basket command. Please use: basket");
					}
					
					printBasket(bookStore);
					break;
					
				case "buy":
					if(tokens.length != 1) {
						printError("Wrong format for the buy command. Please use: buy");
					}
					
					
					List<Book> books = new LinkedList<Book>();
					ShoppingBasket basket = bookStore.getShoppingBasket();
					
					for (ShoppingBasketItem item : basket.getItems()) {
						for (int i = 0; i < item.getQuantity(); i++) {
							books.add(item.getItem());
						}
					}
					
					Book[] bookArray = new Book[books.size()];
					bookArray = books.toArray(bookArray);
					
					int[] results = bookStore.buy(bookArray);
					
					for (int i : results) {
						if(PurchaseStatus.NOT_IN_STOCK.getStatus() == i) {
							printError("Sorry, but the book " + bookArray[i].getAuthor() + "_-_" + bookArray[i].getTitle() + "is out of stock");
						}
					}
					
					break;
					
					
					
//			===================ADMIN=================
				case "addNewUser":
					if(tokens.length != 4) {
						printError("Wrong format for the add new user command. Please use: addNewUser [username] [password] [true|false]");
					}
					
					username = tokens[1];
					password = tokens[2];
					
					if(!tokens[3].equals("true") && !tokens[3].equals("false")) {
						printError("Wrong way of defining a boolean value for the admin. Please provide \"true\" or \"false\"");
					}
					
					boolean admin = Boolean.valueOf(tokens[3]);
					boolean result = bookStore.addNewUser(username, password, admin);
					
					if(result) {
						printResult("User added successfully! Now this user can log in");
					}else {
						printError("The user could not be created. You are not an administer or you provided a wrong password");
					}
					
					break;
					
				case "removeUser":
					
					if(tokens.length != 3) {
						printError("Wrong format for the remove user command. Please use: removeUser [username] [password]");
					}
					
					username = tokens[1];
					password = tokens[2];
					
					result = bookStore.removeUser(username, password);
					
					if(result) {
						printResult("User removed successfully!");
					}else {
						printError("The user could not be removed. You are not an administer or you provided a wrong password");
					}
					break;
					
				case "addBookStore":
					
					if(tokens.length != 4) {
						printError("Wrong format for the add a new book to the store command. Please use: addBookStore [\"title\"] [\"author\"] [price]");
					}
					
					String title = tokens[1];
					String author = tokens[2];
					String price = tokens[3];
					
					result = bookStore.addNewBook(title, author, price);
					
					if(result) {
						printResult("Book added successfully!");
					}else {
						printError("The book could not be added");
					}
					
					break;
					
					
				case "exit":
					loop = false;
					break;
				
				default:
					break;
				}
			}
			
			scanner.close();
			
			
		} catch (BookstoreException e) {
			printError(e.getMessage());
		}
		
	}

	private static void printMenu() {
		System.out.println("BookStore -- What do you need to do? Please type the desired command");
		System.out.println();
		System.out.println("login [username] [password] - Logs in the user to use the access the book store");
		System.out.println("list [searchstring]* - Lists the book available that match the provided search string");
		System.out.println("add [indexBook] [quantity] - Adds the book with the provided index (considering the ones from the last search) to the shopping basket");
		System.out.println("remove [indexBasket] [quantity] - Removes the book with the provided index (considering the ones from the shopping basket) from the shopping basket");
		System.out.println("basket - Prints the current content of the shopping basket");
		System.out.println("buy - Buys the books currently in the shopping basket");

		System.out.println("addNewUser [username] [password] [isAdmin?] - Adds a new user in the bookstore");
		System.out.println("removeUser [username] [password] - Removes a user from the system");
		System.out.println("addBookStore [\"title\"] [\"author\"] [price] - Adds a new book to the system");
		System.out.println();	
		System.out.println("exit - Exits from the system");
		System.out.println();
	}

	private static void printResult(String message) {
		System.out.println(message);
		System.out.println();
	}

	private static void printBasket(BookStore bookStore) {
		ShoppingBasket basket = bookStore.getShoppingBasket();
		
		List<ShoppingBasketItem> items = bookStore.getShoppingBasket().getItems();
		for (int i = 0; i < items.size(); i++) {
			Book book = items.get(i).getItem();
			System.out.println(i + ":" + TAB + book.getAuthor() + " - " + book.getTitle() + TAB + TAB + items.get(i).getQuantity());
		}
		System.out.println("Total: " + basket.getTotal().toString());
		System.out.println();
	}

	private static void printBookList(Book[] result) {
		for (int i = 0; i < result.length; i++) {
			System.out.println(i + ":" + TAB + result[i].getAuthor() + " - " + result[i].getTitle());
		}
		System.out.println();
	}

	private static void printError(String string) {
		System.out.println(string);
		System.out.println();
	}	

}
