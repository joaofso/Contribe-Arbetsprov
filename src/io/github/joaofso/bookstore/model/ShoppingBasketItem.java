package io.github.joaofso.bookstore.model;

/**
 * This class represents an item in the shopping basket. The relationship
 * between the item and the whole basket is important to modularize and increase
 * cohesion.
 * 
 * @author João Felipe
 *
 */
public class ShoppingBasketItem {

	private Book item;
	private int quantity;

	public ShoppingBasketItem(Book item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public Book getItem() {
		return item;
	}

	public void setItem(Book item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean contains(Book book) {
		return this.item.equals(book);
	}

}
