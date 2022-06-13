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
 * Model which represents if a traitment has been taken
 * each traitement is represented by a planning, a date when the traitement has to be taken 
 * the model has also a boolean field to indicate if the traitement has been taken
 */
@Entity
@Table(name = "traitement")
public class Traitement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "idplanning")
	private Integer idplanning;
	
	@ManyToOne
	@JoinColumn(name = "idplanning", insertable = false, updatable = false)
	private Planning planning;
	
	private Timestamp date;
	private Boolean istaken;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdplanning() {
		return idplanning;
	}

	public void setIdplanning(Integer idplanning) {
		this.idplanning = idplanning;
	}
	
	public Planning getPlanning() {
		return planning;
	}
	public void setPlanning(Planning planning) {
		this.planning = planning;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Boolean getIstaken() {
		return istaken;
	}

	public void setIstaken(Boolean istaken) {
		this.istaken = istaken;
	}
	
}
