package ch.hearc.medicalcheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.hearc.medicalcheck.model.Code;
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
public interface CodeRepository extends JpaRepository<Code, Integer>{
	
	public Code findByCode(int code);
}
