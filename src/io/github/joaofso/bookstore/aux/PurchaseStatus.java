package io.github.joaofso.bookstore.aux;

/**
 * This enumeration defines the possible status of the purchase of a book. The
 * buy method will return an array of these responses, one element per book.
 * 
 * @author João Felipe
 *
 */
public enum PurchaseStatus {
	OK(0), 
	NOT_IN_STOCK(1), 
	DOES_NOT_EXIST(2);

	private int status;

	/**
	 * Constructor of the Enumeration.
	 * @param status The numerical codes provided by the exercise.
	 */
	private PurchaseStatus(int status) {
		this.status = status;
	}

	/**
	 * Returns the numerical value of each purchase status.
	 * @return 0 if the purchase was ok, 1 if the book is not in stock, or 2 if it does not exist.
	 */
	int getStatus() {
		return this.status;
	}
}
