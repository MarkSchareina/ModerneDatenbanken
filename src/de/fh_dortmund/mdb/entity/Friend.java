package de.fh_dortmund.mdb.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.j256.ormlite.field.ForeignCollectionField;

@Entity
public class Friend {

	@Id
	//@GeneratedValue
	private int Id;
	
	//@javax.persistence.JoinColumn(columnDefinition )
	private Person PersonId;
	
	private Person FreundId;
	
	
	public Friend(){}
	
}
