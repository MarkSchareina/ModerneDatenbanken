package de.fh_dortmund.mdb.entity;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.j256.ormlite.field.DatabaseField;

@Entity
public class Person{
	
	@Id
	@GeneratedValue
	private int idPerson;
	
	@Column(name ="Nachname")
	private String name;
	
	@Column(name ="Vorname")
	private String firstName;
	
	@Transient
	//@ManyToOne
	//@ManyToOne
	//@DatabaseField(foreign = true)
	@OneToMany
    @JoinTable(
            name="PRODUCT_PARTS",
            joinColumns = @JoinColumn( name="PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn( name="PART_ID")
    )
	private Collection<Person> friends;
	
	public Person() {
		// construction only
		friends = new HashSet<Person>(5);
	}
	
	public Person(String firstName, String name) {
		this.name = name;
		this.firstName = firstName;
		friends = new HashSet<Person>(5);
	}

	@Override
	public String toString() {
		return firstName+ " "+ name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void addFriend(Person friend)
	{
		if(friends.contains(friend)){
			return;
		}
		friends.add(friend);
		friend.addFriend(this);
	}
	
	public Collection<Person> getFriends() {
		return friends;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Person))
			return false;
		Person other = (Person) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
