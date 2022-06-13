package ch.hearc.medicalcheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.hearc.medicalcheck.model.Planning;

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
public interface PlanningRepository extends JpaRepository<Planning, Integer>{
	
	@Query(value="SELECT * FROM planning p WHERE p.idmedicine = ?1", nativeQuery = true)
	public List<Planning> getAllPlanningByIdMedicine(Integer idmedicine);
	
	@Query(value="SELECT * FROM planning p "
			+ "JOIN medicine m ON p.idmedicine = m.id "
			+ "WHERE m.iduser = ?1 "
			+ "AND (p.day IS NULL OR p.day = ?2) "
			+ "AND (CURRENT_DATE >= m.begindate) "
			+ "AND (CURRENT_DATE < m.enddate OR m.enddate IS NULL)", nativeQuery = true)
	public List<Planning> getAllPlanningTodayByIdUser(Integer iduser, String day);
	
	@Query(value="SELECT * FROM planning p "
			+ "JOIN medicine m ON p.idmedicine = m.id "
			+ "WHERE (p.day IS NULL OR p.day = ?1) "
			+ "AND (CURRENT_DATE >= m.begindate) "
			+ "AND (CURRENT_DATE < m.enddate OR m.enddate IS NULL)", nativeQuery = true)
	public List<Planning> getAllPlanningToday(String day);
}
