package ch.hearc.medicalcheck.model;

import javax.persistence.*;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */


/**
 * Model which represents a user
 * a user is either a carekeeper or a patient
 * a carekeeper check if their patient are ok
 * a patient can ask for help if it's needed
 * each user has some useful information to indentify them such as firstname, 
 * login info (username, password),...
 */
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String password;
	private String email;
	private String firstname;
	private String lastname;
	private String picture;
	private boolean iscarekeeper;

	public User() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isIscarekeeper() {
		return iscarekeeper;
	}

	public void setIscarekeeper(boolean iscarekeeper) {
		this.iscarekeeper = iscarekeeper;
	}
	
}
