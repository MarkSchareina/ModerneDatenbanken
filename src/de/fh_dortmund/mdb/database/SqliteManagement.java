package de.fh_dortmund.mdb.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import de.fh_dortmund.mdb.entity.Person;

public class SqliteManagement implements DatabaseManagement {
	private final static String DATABASE_FILE_NAME = "sqlite.db";
	private final static String DRIVER = "org.sqlite.JDBC";
	private final static String URL = "jdbc:sqlite:" + DATABASE_FILE_NAME;

	private static int id = 0;
	
	@Override
	public String getDatabaseName()
	{
		return "SQLite";
	}
	
	@Override
	public void initialize() {
		File databaseFile =	new File(DATABASE_FILE_NAME);
		if (databaseFile.exists()) databaseFile.delete();
		Connection connection = getConnection();
		String sqlPerson = "CREATE TABLE IF NOT EXISTS Person (idPerson INT NOT NULL PRIMARY KEY, Nachname VARCHAR(45) NULL, Vorname VARCHAR(45) NULL)";
		String sqlFriends = "CREATE TABLE IF NOT EXISTS Freunde (PersonId INT NOT NULL, FreundId INT NOT NULL,CONSTRAINT personFK FOREIGN KEY (PersonId) REFERENCES Person (idPerson), CONSTRAINT freundeFK FOREIGN KEY (FreundId) REFERENCES Person (idPerson))";
		try {
			connection.createStatement().executeUpdate(sqlPerson);
			connection.createStatement().executeUpdate(sqlFriends);
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void store(Person person) {
		try {
			Connection con = getConnection();

			final String sqlInsertPerson = "INSERT INTO Person (idPerson, Nachname, Vorname) VALUES (?, ?, ?);";
			PreparedStatement stm = con.prepareStatement(sqlInsertPerson);
			stm.setInt(1, id++);
			stm.setString(2, person.getName());
			stm.setString(3, person.getFirstName());
			stm.executeUpdate();
			stm.close();

			con.close();
			if (person.getFriends() != null || !person.getFriends().isEmpty()) {
				for (Person friend : person.getFriends()) {
					long id = getId(friend);
					if (-1L == id) {
						store(friend);
					}
					addAsFriends(person, friend);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Collection<Person> getFriends(String firstname) {
		Set<Person> friends = new HashSet<>();
		try {
			Connection con = getConnection();
			String sqlSelect = "SELECT p.idPerson, p.Nachname, p.Vorname FROM person p WHERE p.idPerson IN (SELECT f.PersonId FROM Freunde f JOIN  Person p2 ON f.FreundId = p2.idPerson WHERE p2.Vorname = ?)";
			PreparedStatement stm = con.prepareStatement(sqlSelect);
			stm.setString(1, firstname);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				Person friend = new Person(rs.getString("Vorname"), rs.getString("Nachname"));
				friends.add(friend);
			}
			stm.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friends;
	}

	private void addAsFriends(Person person, Person friend) {
		final String sqlInsertFriend = "INSERT INTO Freunde (PersonId, FreundId) VALUES (?, ?);";
		try {
			Connection con = getConnection();
			PreparedStatement stm = con.prepareStatement(sqlInsertFriend);
			stm.setInt(1, getId(person));
			stm.setInt(2, getId(friend));
			stm.executeUpdate();
			stm.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private int getId(Person p){
		final String sqlSelect = "SELECT idPerson FROM person WHERE Nachname=? and Vorname=?";
		int id = -1;
		try {
			Connection con = getConnection();
			PreparedStatement stm = con.prepareStatement(sqlSelect);
			stm.setString(1, p.getName());
			stm.setString(2, p.getFirstName());
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				id = rs.getInt("idPerson");
			}
			stm.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	private static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

}
