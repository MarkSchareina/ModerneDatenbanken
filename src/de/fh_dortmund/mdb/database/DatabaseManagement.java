package de.fh_dortmund.mdb.database;

import java.util.Collection;

import de.fh_dortmund.mdb.entity.Person;

public interface DatabaseManagement {

	String getDatabaseName();
	
	void initialize();
	
	void store(Person person);
	
	Collection<Person> getFriends(String firstname);
}