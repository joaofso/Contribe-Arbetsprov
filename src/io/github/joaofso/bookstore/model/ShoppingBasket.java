package io.github.joaofso.bookstore.model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the behavior of the shopping basket. It stores the
 * items, their quantities and the purchase total. Here, one can add and remove
 * items.
 * 
 * @author João Felipe
 *
 */
public class ShoppingBasket {

	private User buyer;
	private List<ShoppingBasketItem> items;
	private BigDecimal total;

	public ShoppingBasket() {
		this.items = new LinkedList<ShoppingBasketItem>();
		this.total = new BigDecimal("0");
	}

	/**
	 * Add a book to the shopping basket.
	 * 
	 * @param book
	 *            The book to be added.
	 * @return A boolean value indicating whether the book was added successfully.
	 */
	public boolean addItem(Book book) {
		if (this.basketLimit()) {
			return false;
		}

		ShoppingBasketItem item = this.getItem(book);
		if (item == null) {
			item = new ShoppingBasketItem(book, 0);
			this.items.add(item);
		}

		item.setQuantity(item.getQuantity() + 1);
		this.total = this.total.add(book.getPrice());
		return true;
	}

	/**
	 * Returns the current total of the shopping basket.
	 * 
	 * @return A bigdecimal value indicating the sum of all items in the shopping
	 *         basket.
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * Removes a book from the shopping basket.
	 * 
	 * @param book The book to be removed from the basket.
	 * @return A boolean value indicating whether the book was removed from the
	 *         basket. If the returned value is false, the book was not in the
	 *         shopping basket in the first place.
	 */
	public boolean removeItem(Book book) {
		ShoppingBasketItem item = this.getItem(book);
		if (item == null) {
			return false;
		}

		if (item.getQuantity() == 1) {
			this.items.remove(item);
		} else {
			item.setQuantity(item.getQuantity() - 1);
		}
		this.total = this.total.subtract(book.getPrice());
		return true;
	}

	private ShoppingBasketItem getItem(Book book) {
		for (ShoppingBasketItem shoppingBasketItem : items) {
			if (shoppingBasketItem.contains(book)) {
				return shoppingBasketItem;
			}
		}

		return null;
	}

	/**
	 * Stub method to include a rule to limit the basket size.
	 * 
	 * @return
	 */
	private boolean basketLimit() {
		return false;
	}
}
