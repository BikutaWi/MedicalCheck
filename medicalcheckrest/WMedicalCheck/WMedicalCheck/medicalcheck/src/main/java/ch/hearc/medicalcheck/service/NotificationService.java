
package ch.hearc.medicalcheck.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.medicalcheck.model.Notification;
import ch.hearc.medicalcheck.repository.NotificationRepository;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * bridge between repository and controller
 * each methods is explained in the associate controller
 * check them for more information
 */

@Service
public class NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;

	public List<Notification> getAll() {
		return notificationRepository.findAll();
	}

	public Notification get(Integer id) {
		return notificationRepository.findById(id).get();
	}

	public List<Notification> getAllByIdUser(Integer iduser) {
		return notificationRepository.getAllNotificationByIdUser(iduser);
	}

	public Notification create(Notification notification) {
		return notificationRepository.save(notification);
	}

	public Notification update(Integer idNotification, Notification notification) {
		Optional<Notification> notificationData = notificationRepository.findById(notification.getId());

		if (notificationData.isPresent()) {
			notification.setId(idNotification);
			return notificationRepository.save(notification);
		}
		return null; 
	}

	public void delete(Integer id) {
		notificationRepository.deleteById(id);
	}
}
