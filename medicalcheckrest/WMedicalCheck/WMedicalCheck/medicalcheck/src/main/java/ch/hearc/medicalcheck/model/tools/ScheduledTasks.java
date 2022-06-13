package ch.hearc.medicalcheck.model.tools;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.hearc.medicalcheck.model.Notification;
import ch.hearc.medicalcheck.model.Planning;
import ch.hearc.medicalcheck.model.Traitement;
import ch.hearc.medicalcheck.model.User;
import ch.hearc.medicalcheck.repository.NotificationRepository;
import ch.hearc.medicalcheck.repository.PlanningRepository;
import ch.hearc.medicalcheck.repository.TraitementRepository;
import ch.hearc.medicalcheck.repository.UserRepository;
import ch.hearc.medicalcheck.service.UserService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

@Component
public class ScheduledTasks {
	@Autowired
	private PlanningRepository planningRepository;

	@Autowired
	private TraitementRepository traitementRepository;

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private UserRepository UserRepository;

	/**
	 * everyday at midnight a task is executed to create the traitement that a user has to take
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void createTraitement() {

		System.out.println("Create new traitement");

		String today = LocalDate.now().getDayOfWeek().toString();

		// get all today's plannings
		List<Planning> list = planningRepository.getAllPlanningToday(today);

		Function<Planning, Traitement> getTraitement = new Function<Planning, Traitement>() {

			@Override
			public Traitement apply(Planning p) {
				Traitement traitement = new Traitement();
				traitement.setIdplanning(p.getId());
				traitement.setIstaken(false);
				traitement.setDate(new Timestamp(System.currentTimeMillis()));

				return traitement;
			}
		};

		// create new traitement for each planning
		list.stream().parallel().map(getTraitement).forEach(t -> traitementRepository.save(t));
	}

	/**
	 * check every minute if a user has not taken his traitement, and if it's the case
	 * create a notification to alert the user
	 */
	@Scheduled(cron = "0 * * * * *")
	public void createNotification() {

		List<Traitement> list = traitementRepository.getAllTreatmentCurrentTime();

		Function<Traitement, Notification> getNotification = new Function<Traitement, Notification>() {

			@Override
			public Notification apply(Traitement t) {
				
				//create the notification
				Notification notification = new Notification();
				notification.setIduser(t.getPlanning().getMedicine().getIduser());
				notification.setIdusertonotify(t.getPlanning().getMedicine().getIduser());
				notification.setNotificationtype(NotificationType.TREATMENT_NOT_TAKEN);
				notification.setIsclosed(false);
				notification.setDate(Timestamp.from(Instant.now()));

				return notification;
			}
		};

		list.stream().parallel().map(getNotification).forEach(n -> 
		{
			//save the notification
			Notification notification = notificationRepository.save(n);
			
			//get all my carekeepers and notify them
			List<User> careKeepers = UserRepository.getAllMyCarekeeper(n.getIduser());
			careKeepers.forEach(careKeeper -> notificationRepository.save(notification.notifyTo(careKeeper.getId())));					
		});
	}

}
