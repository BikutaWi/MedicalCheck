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

@Entity
@Table(name = "code")

/**
 * model which represent a code
 * a code is attaced to a user
 * a code allow the user to connect to his watch quickly
 * he don't need to type his username and password, but instead has to generate this code
 * on the mobile app, the code is valid for 30s
 * a code is generate randomly between 100000-999999
 */
public class Code {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "iduser", insertable = true, updatable = true)
	private User user;
	
	@Column(unique=true)
	private Integer code;
	

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public User getUser() {
		return this.user;
	}


	public void setUser(User user) {
		this.user = user;
	}
 

	public Integer getCode() {
		return code;
	}


	public void setCode(Integer code) {
		this.code = code;
	}

	public Code() {
	}

	
}
