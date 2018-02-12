#!/bin/sh
ant build-project
current_path=`pwd`
clear
java -classpath $current_path/build/classes:./lib/jdom-2.0.4.jar io.github.joaofso.bookstore.aux.BookStoreSys