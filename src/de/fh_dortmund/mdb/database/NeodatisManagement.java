package de.fh_dortmund.mdb.database;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.impl.core.query.criteria.EqualCriterion;

import de.fh_dortmund.mdb.entity.Person;

public class NeodatisManagement implements DatabaseManagement {
	private static final String DATABASE_FILE_NAME = "./neodatis.db";
	
	@Override
	public String getDatabaseName()
	{
		return "NeoDatis";
	}
	
	@Override
	public void initialize() {
		File databaseFile =	new File(DATABASE_FILE_NAME);
		if (databaseFile.exists()) databaseFile.delete();
	}

	@Override
	public void store(Person person) {
		ODB connection = getConnection();
		connection.store(person);
		connection.close();
	}

	@Override
	public Collection<Person> getFriends(String firstname) {
		ODB connection = getConnection();
		IQuery query = connection.criteriaQuery(Person.class, new EqualCriterion("firstName", firstname));
		Objects<Person> objects = connection.getObjects(query);
		connection.close();
		Collection<Person> friends = new HashSet<Person>();
		for (Person person : objects) {
			friends.addAll(person.getFriends());
		}
		return friends;
	}
	
	private ODB getConnection()
	{
		ODB odb = ODBFactory.open(DATABASE_FILE_NAME);
		return odb;
	}
}