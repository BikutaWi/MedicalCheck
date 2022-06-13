package ch.hearc.medicalcheck.model;

import java.sql.Timestamp;

import javax.persistence.*;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */


/**
 * Model which represents a mesure
 * a mesure is used to display some information about a heart rate
 * a measure is attached to a user, a heartrate and a date when the measure has been recorded
 */
@Entity
@Table(name = "measure")
public class Measure {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer iduser;
	private Integer heartrate;
	private Timestamp date;

	public Measure() {
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

	public Integer getHeartrate() {
		return heartrate;
	}

	public void setHeartrate(Integer heartrate) {
		this.heartrate = heartrate;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	
}
