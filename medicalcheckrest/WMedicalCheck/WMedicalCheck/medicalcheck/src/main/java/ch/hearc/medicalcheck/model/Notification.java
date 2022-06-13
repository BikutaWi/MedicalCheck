package ch.hearc.medicalcheck.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import ch.hearc.medicalcheck.model.tools.NotificationType;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Model which represents a notification
 * a notification is used to display some important information for our users
 * such as a treatment which has not been taken or a help request
 * each notification is composed of :
 * a type (help, treatment not taken,...)
 * some location coordinates to find the user if he needs help
 * the user to notifiy 
 * a date when the notification was created
 * and a boolean (isclosed) which indicate if the notification has been treated (sent)
 */
@Entity
@Table(name = "notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer iduser;
	
	@Nullable
	private Integer idusertonotify;

	@Enumerated(EnumType.STRING)
	@Column(name = "notificationtype")
	private NotificationType notificationtype;

	@Column(name = "date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp date;

	@Nullable
	private Double longitude;
	
	@Nullable
	private Double latitude;

	@Column(columnDefinition = "boolean default false")
	private Boolean isclosed;

	public Notification() {
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

	public Integer getIdusertonotify() {
		return idusertonotify;
	}

	public void setIdusertonotify(Integer idusertonotify) {
		this.idusertonotify = idusertonotify;
	}

	public NotificationType getNotificationtype() {
		return notificationtype;
	}

	public void setNotificationtype(NotificationType notificationtype) {
		this.notificationtype = notificationtype;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Boolean getIsclosed() {
		return isclosed;
	}

	public void setIsclosed(Boolean isclosed) {
		this.isclosed = isclosed;
	}

	public Notification notifyTo(int idusertonotify) {
		Notification notificationClone = new Notification();
		notificationClone.iduser = this.iduser;
		notificationClone.idusertonotify = idusertonotify;
		notificationClone.date = this.date;
		notificationClone.latitude = this.latitude;
		notificationClone.longitude = this.longitude;
		notificationClone.notificationtype = this.notificationtype;
		notificationClone.isclosed = this.isclosed;

		return notificationClone;
	}

}
