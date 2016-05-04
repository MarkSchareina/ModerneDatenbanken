package de.fh_dortmund.mdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.neodatis.odb.Objects;

import de.fh_dortmund.mdb.database.DatabaseManagement;
import de.fh_dortmund.mdb.database.NeodatisManagement;
import de.fh_dortmund.mdb.database.OrmliteManagement;
import de.fh_dortmund.mdb.database.SqliteManagement;
import de.fh_dortmund.mdb.entity.Event;
import de.fh_dortmund.mdb.entity.Person;

public class Program {
	
	public static void main(String[] args) throws Exception {
		final String firstname = "Mark-42";
		
		DatabasePerformanceBenchmarkTest benchmark = new DatabasePerformanceBenchmarkTest();
		
		DatabaseManagement[] databaseManagements = new DatabaseManagement[] { 
				//new NeodatisManagement(),
				//new SqliteManagement(),
				new OrmliteManagement()
		};
		
		prepareAndTest(benchmark, databaseManagements, firstname);
		
		for (DatabaseManagement databaseManagement : databaseManagements) {
			long benchmarkValue = benchmark.benchmark(databaseManagement, firstname);
			System.out.format("%10s: %d\n", databaseManagement.getDatabaseName(), benchmarkValue);
		}
			
	}
	
	public static void prepareAndTest(DatabasePerformanceBenchmarkTest benchmark, DatabaseManagement[] databaseManagements, String firstname)
	{
		final int nMax = 500;
		final String firstnameBase = "Mark";
		
		benchmark.generatePersonsWithFriends(nMax, firstnameBase);
		for (DatabaseManagement databaseManagement : databaseManagements) {
			benchmark.prepare(databaseManagement);
			benchmark.test(databaseManagement, firstname);		
		}
	}
		
//		
//		
//		try {
//			System.out.println("==== NEODATIS ====");
//			//neoDatis.neoDatisStore(p.toArray());
//			// neoDatisQuery(C);
//			Collection<Person> friends = NeodatisManagement.getFriends(name);
//			printFriends(friends);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("FEHLER");
//		}
//		System.out.println("\n\n");
//		System.out.println("==== SQLITE ====");
//		//sqlite.initTable();
//		//sqlite.store(p.get(0));
//		Set<Person> friends2 = SqliteManagement.getFriends(name);
//		System.out.println("===============================");
//		System.out.println("Mit Person " + name + " sind folgende Personen befreundet:");
//		printFriends(friends2);
	
	
	private static List<Person> createPersonsWithFriends(int n)
	{
		List<Person> persons = new ArrayList<Person>();
		for (int i = 0; i < n; i++) {
			Person px = new Person("M" + i, "Test");
			if (i > 1) {
				px.addFriend(persons.get(i - 1));
				px.addFriend(persons.get(i - 2));
			}
			persons.add(px);
		}
		return persons;
	}

	public static void printEvents(Person person) {
		System.out.println(person + " nimmt an folgenden Events teil:");

		
	}

	public static void printMembers(Event event) {
		System.out.println("An der Veranstalltung \"" + event.getTitle() + "\" nehmen folgenden Personen teil:");
		for (Person p : event.getMembers()) {
			System.out.println(p);
		}
	}

	public static void printFriends(Objects<Person> persons) {
		System.out.println("===============================");
		for (Person person : persons) {
			System.out.println("Person " + person + " ist befreundet mit:");
			for (Person friends : person.getFriends()) {
				System.out.println(friends);
			}
			System.out.println("===============================");
		}
	}

	public static void printFriends(Set<Person> friends) {
		System.out.println("===============================");
		for (Person p : friends) {
			System.out.println(p);
		}
		System.out.println("===============================");
	}

}
