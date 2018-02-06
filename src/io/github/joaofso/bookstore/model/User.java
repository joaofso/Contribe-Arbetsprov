package io.github.joaofso.bookstore.model;

/**
 * This class represents the user entity, with username, password and a simple
 * value indicating whether the user is an administrator.
 * 
 * @author João Felipe
 *
 */
public class User {

	private String username;
	private String password;
	private boolean admin;

	public User(String username, String password, boolean admin) {
		super();
		this.setUsername(username);
		this.setPassword(password);
		;
		this.setAdmin(admin);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof User)) {
			return false;
		}
		User otherUser = (User) obj;
		
		return this.getUsername().equals(otherUser.getUsername()) && 
				this.getPassword().equals(otherUser.getPassword()) && 
				this.isAdmin() == otherUser.isAdmin();
	}

}
