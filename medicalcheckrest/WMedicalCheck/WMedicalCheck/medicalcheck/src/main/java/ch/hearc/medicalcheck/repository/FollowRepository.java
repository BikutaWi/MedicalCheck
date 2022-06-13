package ch.hearc.medicalcheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.hearc.medicalcheck.model.Follow;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * each methods is explained in the associate controller
 * check them for more information
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer>{
	
	@Query(value="SELECT * FROM follow f WHERE f.idusercarekeeper = ?1", nativeQuery = true)
	public List<Follow> getAllFollowByIdUserCareKeeper(Integer idUserCareKeeper);
	
	@Query(value="SELECT * FROM follow f WHERE f.iduserpatient = ?1", nativeQuery = true)
	public List<Follow> getAllFollowByIdUserPatient(Integer idUserPatient);
}
