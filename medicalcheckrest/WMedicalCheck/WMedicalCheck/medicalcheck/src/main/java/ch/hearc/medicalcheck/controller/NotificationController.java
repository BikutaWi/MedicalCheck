
package ch.hearc.medicalcheck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.medicalcheck.model.Notification;
import ch.hearc.medicalcheck.model.User;
import ch.hearc.medicalcheck.service.NotificationService;
import ch.hearc.medicalcheck.service.UserService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */


/**
 * Controller to manage all request
 *  for the notification ressources
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {
	@Autowired
	NotificationService notificationService;

	@Autowired
	UserService userService;

	/**
	 * list all notification
	 */
	@GetMapping("")
	public List<Notification> getAll() {
		return notificationService.getAll();
	}

	/**
	 * get a notification by id
	 */
	@GetMapping("{id}")
	public Notification getNotification(@PathVariable int id) {
		return notificationService.get(id);
	}

	/**
	 * list all notification for a user
	 */
	@GetMapping("/user/{iduser}")
	public List<Notification> getAllByIdUser(@PathVariable int iduser) {
		return notificationService.getAllByIdUser(iduser);
	}

	/**
	 * update a notification by id
	 */
	@PutMapping("{id}")
	public Notification update(@PathVariable int id, @RequestBody Notification newNotification) {
		return notificationService.update(id, newNotification);
	}

	/**
	 * create a notification 
	 */
	@PostMapping("")
	public Notification create(@RequestBody Notification newNotification) {
		
		//indicate that the user which has created the notification has to be notifiy
		newNotification.setIdusertonotify(newNotification.getIduser());
		
		//save the notification
		Notification notification = notificationService.create(newNotification);
		
		//get all carekeepers which are related to this user
		List<User> myCarekeepers = userService.getAllMyCarekeeper(notification.getIduser());
		for (User carekeeper : myCarekeepers) {
			//copy the notification to notifiy the carekeeper
			notificationService.create(notification.notifyTo(carekeeper.getId()));
		}
		return notification;
	}

	/**
	 * delete a notification by id
	 */
	@DeleteMapping("{id}")
	public void delete(@PathVariable int id) {
		notificationService.delete(id);
	}

}
