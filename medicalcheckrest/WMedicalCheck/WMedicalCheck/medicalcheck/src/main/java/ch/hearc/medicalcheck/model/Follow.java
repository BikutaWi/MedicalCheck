package ch.hearc.medicalcheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Model which represents a the relation between users
 * a user can be patient or carekeeper
 * this model associate each patien with their carekeeper
 * a relation is also define by a relation (for example, friends)
 */

@Entity
@Table(name = "follow")
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "iduserpatient")
	private Integer iduserpatient;
	
	@ManyToOne()
	@JoinColumn(name = "iduserpatient", insertable = false, updatable = false)
	private User userpatient;
	
	@Column(name = "idusercarekeeper")
	private Integer idusercarekeeper;
	
	@ManyToOne()
	@JoinColumn(name = "idusercarekeeper", insertable = false, updatable = false)
	private User usercarekeeper;

	private String relationship;
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getIduserpatient() {
		return iduserpatient;
	}


	public void setIduserpatient(Integer iduserpatient) {
		this.iduserpatient = iduserpatient;
	}

	public User getUserpatient() {
		return userpatient;
	}


	public void setUserpatient(User userpatient) {
		this.userpatient = userpatient;
	}

	public Integer getIdusercarekeeper() {
		return idusercarekeeper;
	}


	public void setIdusercarekeeper(Integer idusercarekeeper) {
		this.idusercarekeeper = idusercarekeeper;
	}
	
	public User getUsercarekeeper() {
		return usercarekeeper;
	}


	public void setUsercarekeeper(User usercarekeeper) {
		this.usercarekeeper = usercarekeeper;
	}

	public String getRelationship() {
		return relationship;
	}


	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	
}
