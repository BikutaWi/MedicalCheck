package ch.hearc.medicalcheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.hearc.medicalcheck.model.Traitement;

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
public interface TraitementRepository extends JpaRepository<Traitement, Integer>{
	
	@Query(value="SELECT * FROM traitement t WHERE t.idplanning = ?1", nativeQuery = true)
	public List<Traitement> getAllTreatmentByIdPlanning(Integer idplanning);
	
	@Query(value="SELECT * FROM traitement t "
			+ "JOIN planning p ON t.idplanning = p.id "
			+ "JOIN medicine m ON p.idmedicine = m.id "
			+ "WHERE m.iduser = ?1 "
			+ "AND CAST(t.date AS Date) = CURRENT_DATE "
			+ "AND t.istaken = 0", nativeQuery = true)
	public List<Traitement> getAllTreatmentByIdUser(Integer iduser);
	
	@Query(value="SELECT * FROM `traitement` t "
			+ "JOIN `planning` p ON t.idplanning = p.id "
			+ "JOIN `medicine` m ON p.idmedicine = m.id "
			+ "WHERE DATE(t.date) = DATE(NOW()) "
			+ "AND t.istaken = 0 "
			+ "AND p.time = NOW()", nativeQuery = true)
	public List<Traitement> getAllTreatmentCurrentTime();
	
	@Query(value="SELECT COUNT(*) FROM traitement t "
			+ "JOIN planning p ON t.idplanning = p.id "
			+ "JOIN medicine m ON p.idmedicine = m.id "
			+ "WHERE t.istaken = 0 "
			+ "AND DATE(t.date) = DATE(NOW()) "
			+ "AND p.time < NOW() "
			+ "AND m.iduser = ?1", nativeQuery = true)
	public Integer countTreatmentNotTaken(Integer iduser);
}
