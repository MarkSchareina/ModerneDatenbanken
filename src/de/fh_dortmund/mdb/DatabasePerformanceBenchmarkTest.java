package de.fh_dortmund.mdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.fh_dortmund.mdb.database.DatabaseManagement;
import de.fh_dortmund.mdb.entity.Person;

public class DatabasePerformanceBenchmarkTest {
	private final List<Person> persons = new ArrayList<Person>();
	
	public List<String> generatePersonsWithFriends(int nMax, String firstnameBase)
	{
		List<String> firstnames = new ArrayList<String>();
		persons.clear();
		for (int i = 0; i < nMax; i++) {
			String firstname = String.format("%s-%d", firstnameBase, i);
			firstnames.add(firstname); 
			Person px = new Person(firstname, "Test");
			if (i > 1) {
				px.addFriend(persons.get(i - 1));
				px.addFriend(persons.get(i - 2));
			}
			persons.add(px);
		}
		return firstnames;
	}
	
	public void prepare(DatabaseManagement databaseManagement)
	{
		databaseManagement.initialize();
		databaseManagement.store(persons.get(0));
	}
	
	public long benchmark(DatabaseManagement databaseManagement, String firstname)
	{
		long t1, t2;
		t1 = System.nanoTime();
		Collection<Person> friends = databaseManagement.getFriends(firstname);
		t2 = System.nanoTime();
		friends.clear();
		return t2 - t1;
	}
	
	public void test(DatabaseManagement databaseManagement, String firstname)
	{
		Collection<Person> friends = databaseManagement.getFriends(firstname);
		System.out.println("===============================");
		System.out.println("Test database: " + databaseManagement.getDatabaseName());
		System.out.println("Friends from persons with firstname: " + firstname);
		for (Person friend : friends) {
			System.out.println(friend);
		}
		System.out.println("===============================");
		
	}
	
	
}
