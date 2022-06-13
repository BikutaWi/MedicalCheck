package ch.hearc.medicalcheck.repository;

import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.hearc.medicalcheck.model.Measure;

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
public interface MeasureRepository extends JpaRepository<Measure, Integer>{
	
	@Query(value="SELECT * FROM measure m WHERE m.iduser = ?1 ORDER BY m.date ASC", nativeQuery = true)
	public List<Measure> getAllMeasureByIdUser(Integer iduser);
	
	@Query(value="SELECT * FROM measure m WHERE m.iduser = ?1 ORDER BY m.date DESC LIMIT 1", nativeQuery = true)
	public Measure getLastMeasureByIdUser(Integer iduser);
	
	@Query(value="SELECT HOUR(m.date), CAST(avg(m.heartrate) AS DOUBLE) FROM measure m "
			+ "WHERE m.iduser = ?1 AND CAST(m.date AS DATE) = ?2 "
			+ "GROUP BY CAST(m.date AS DATE), HOUR(m.date) "
			+ "ORDER BY HOUR(m.date) DESC", nativeQuery = true)
	public List<Tuple> getMapAverageMeasure(Integer iduser, String date);
}
