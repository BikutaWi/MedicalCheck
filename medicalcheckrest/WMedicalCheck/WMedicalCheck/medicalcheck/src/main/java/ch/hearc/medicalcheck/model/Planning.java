
package ch.hearc.medicalcheck.model;

import java.sql.Time;
import java.time.DayOfWeek;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 *  Model which represents a planning for a medicine
 *  a medicine can be taken every day or some particular day
 *  so this entity allow to indicate the frequency to take the medicine
 *  each planning is associate with one ore many days
 *  a medicine, a time when to take the medicine, and a quantity (ex 3x  for dafalgan 1000mg)
 */
@Entity
@Table(name = "planning")
public class Planning {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "idmedicine")
	private Integer idmedicine;

	@ManyToOne
	@JoinColumn(name = "idmedicine", insertable = false, updatable = false)
	private Medicine medicine;

	@Enumerated(EnumType.STRING)
	@Column(name = "day")
	@Nullable
	private DayOfWeek day;

	@JsonFormat(pattern = "HH:mm:ss")
	// @JsonDeserialize(using = SqlTimeDeserializer.class)
	private Time time;

	private Integer quantity;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdmedicine() {
		return idmedicine;
	}

	public void setIdmedicine(Integer idmedicine) {
		this.idmedicine = idmedicine;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public DayOfWeek getDay() {
		return day;
	}

	public void setDay(DayOfWeek day) {
		this.day = day;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
