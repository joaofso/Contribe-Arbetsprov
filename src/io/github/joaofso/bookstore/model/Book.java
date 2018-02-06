package io.github.joaofso.bookstore.model;

import java.math.BigDecimal;

/**
 * This class represents the book entity, with title, author and price, as
 * proposed in the exercise.
 * 
 * @author João Felipe
 *
 */
public class Book {

	private String title;
	private String author;
	private BigDecimal price;

	public Book(String title, String author, BigDecimal price) {
		this.setTitle(title);
		this.setAuthor(author);
		this.setPrice(price);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Book)) {
			return false;
		}
		Book toCompare = (Book) obj;
		return 	this.getTitle().equals(toCompare.getTitle()) &&
				this.getAuthor().equals(toCompare.getAuthor()) &&
				this.getPrice().equals(toCompare.getPrice());
	}
}
