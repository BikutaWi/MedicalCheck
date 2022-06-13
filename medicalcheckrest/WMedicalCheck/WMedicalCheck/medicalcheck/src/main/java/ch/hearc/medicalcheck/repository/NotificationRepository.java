
package ch.hearc.medicalcheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.hearc.medicalcheck.model.Notification;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * extends each CRUD method, and add usefull methods 
 * each methods is explained in the associate controller
 * check them for more information
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{
	
	@Query(value="SELECT * FROM notification n WHERE n.idusertonotify = ?1 AND n.isclosed = 0", nativeQuery = true)
	public List<Notification> getAllNotificationByIdUser(Integer iduser);
}
