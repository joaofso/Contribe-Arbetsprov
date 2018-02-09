package io.github.joaofso.bookstore.control;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

@RunWith(JUnitPlatform.class)
@SelectClasses({
	io.github.joaofso.bookstore.control.dao.impl.BookDAOXMLTest.class,
	io.github.joaofso.bookstore.control.dao.impl.UserDAOXMLTest.class,
	io.github.joaofso.bookstore.control.BookStoreTest.class
})
public class AllTests {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(AllTests.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure);
		}
		System.out.println(result.wasSuccessful());
	}
	
}
