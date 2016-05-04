package de.fh_dortmund.mdb.entity;

import java.util.Collection;
import java.util.HashSet;

public class Event {

	private String title;
	
	private Collection<Person> members;
	
	public Event(String title) {
		this.title = title;
		members = new HashSet<Person>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void addMember(Person member)
	{	
		members.add(member);
		//member.addEvent(this);
	}
	

	public Collection<Person> getMembers()
	{
		return members; 
	}
	

}
