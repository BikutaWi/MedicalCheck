package ch.hearc.medicalcheck.model;

import java.sql.Timestamp;

import javax.persistence.*;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Model which represents a medicine
 * a medicine is created by a user
 * each medicine is define by 
 * a name, a date when he has to start to take this medicine
 * an optional ending date (if no date is set that's mean the user has to keep on taking this medicine
 * a dose to indicate the amount to take
 */
@Entity
@Table(name = "medicine")
public class Medicine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "iduser")
	private Integer iduser;
	
	@ManyToOne()
	@JoinColumn(name = "iduser", insertable = false, updatable = false)
	private User user;
	
	private String name;
	private Timestamp begindate;
	
	@Nullable
	private Timestamp enddate;
	private String dose;

	public Medicine() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIduser() {
		return iduser;
	}

	public void setIduser(Integer iduser) {
		this.iduser = iduser;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getBegindate() {
		return begindate;
	}

	public void setBegindate(Timestamp begindate) {
		this.begindate = begindate;
	}

	public Timestamp getEnddate() {
		return enddate;
	}

	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	@Override
	public String toString() {
		return "Medicine [id=" + id + ", name=" + name + ", begindate=" + begindate
				+ ", enddate=" + enddate + ", dose=" + dose + "]";
	}
}
