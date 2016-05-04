package de.fh_dortmund.mdb.database;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.fh_dortmund.mdb.entity.Person;

public class OrmliteManagement  implements DatabaseManagement{
	private interface PersonDao extends Dao<Person, Long>{}
	private class PersonDaoImpl extends BaseDaoImpl<Person, Long> implements PersonDao{
		public PersonDaoImpl(ConnectionSource conn) throws SQLException{
			super (conn, Person.class);
		}
	}
	
	private static final String DATABASE_FILE_NAME = "ormlitewithsqlite.db";
	//public final static String DRIVER = "org.sqlite.JDBC";
	public final static String URL = "jdbc:sqlite:" + DATABASE_FILE_NAME;
	
	@Override
	public String getDatabaseName() {
		return "OrmLite";
	}
	
	@Override
	public void initialize() {
		File databaseFile =	new File(DATABASE_FILE_NAME);
		if (databaseFile.exists()) databaseFile.delete();
		try {
			TableUtils.createTableIfNotExists(getConnection(), Person.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void store(Person person) {
		try {
			getPersonDao().create(person);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Collection<Person> getFriends(String firstname) {
		List<Person> friends = null;
		try {
			QueryBuilder<Person, Long> personsWithFirstnameMatch = getPersonDao().queryBuilder();
			personsWithFirstnameMatch.where().eq("Vorname" ,firstname);
			
			System.out.println(personsWithFirstnameMatch.query());
			
//			
//			Person friendToCompare = new Person();
//			friendToCompare.setFirstName(firstname);
//			
//			Person person = new Person();
//			person.addFriend(friendToCompare);
//						
//					
//					where()., value)    query();
//			
//			
//			
//			friends = getPersonDao().queryBuilder().    query();
//			
//			
//			
//					
//					queryBuilder().where()
//					.eq("vorname", "Lise“).query()
//					queryForMatching(person);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friends;
	}
	
	private PersonDao getPersonDao()
	{
		PersonDao personDao = null;
		try {
			// Object s = com.j256.ormlite.dao.DaoManager.createDao(getConnection(), Person.class);
			personDao = new PersonDaoImpl(getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return personDao;
	}
	
	private JdbcConnectionSource getConnection()
	{	
		JdbcConnectionSource connection = null;
		try {
			connection = new JdbcConnectionSource(URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}	
}
