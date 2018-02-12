# Contribe-Arbetsprov
Test performed as an assignment for the Contribe selection process.


As proposed in the assignment, I was not supposed to use external libraries unless justified. Therefore, I've used **jdom2** as a simplification to manipulate XML in the project, since I opted to store the entities (books and users) in XML files since it is a simple project.

I have implemented the server side of the BookStore. It includes the logic, which covers the models to be stored in databases and controllers that operates with the model elements: users and books. The current stage is ready to receive servlets to treat the data coming from the web pages.

I also provide an ANT file, called build.xml, which is able to build and run a simple shell that alows to use the bookstore. Besides, I also provide a BookStore.sh, which executes the class in a dedicated terminal, which leaves to screen remains from ANT.

I have implemented the following commands:

* login [username] [password] - Logs in the user to use the access the book store
* list [searchstring]* - Lists the book available that match the provided search string
* add [indexBook] [quantity] - Adds the book with the provided index (considering the ones from the last search) to the shopping basket
* remove [indexBasket] [quantity] - Removes the book with the provided index (considering the ones from the shopping basket) from the shopping basket
* basket - Prints the current content of the shopping basket
* buy - Buys the books currently in the shopping basket
* addNewUser [username] [password] [isAdmin?] - Adds a new user in the bookstore
* removeUser [username] [password] - Removes a user from the system 
* addBookStore ["title"] ["author"] [price] - Adds a new book to the system
* exit - Exits from the system 