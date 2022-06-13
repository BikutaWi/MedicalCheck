package ch.hearc.medicalcheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.hearc.medicalcheck.model.User;

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
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(value = "SELECT * FROM user u WHERE u.username = ?1", nativeQuery = true)
	public User getByUserName(String username);

	@Query(value = "SELECT * FROM user u " + "WHERE u.iscarekeeper = 1 " + "AND u.id NOT IN "
			+ "(SELECT idusercarekeeper FROM follow WHERE iduserpatient = ?1) AND u.id != ?1", nativeQuery = true)
	public List<User> getAllAvailableCarekeeper(int iduser);

	@Query(value = "SELECT * FROM user " + "WHERE id IN "
			+ "(SELECT idusercarekeeper FROM follow WHERE iduserpatient=?1)", nativeQuery = true)
	public List<User> getAllMyCarekeeper(int iduser);
}
