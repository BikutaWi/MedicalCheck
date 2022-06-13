package ch.hearc.medicalcheck.controller;

import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.medicalcheck.model.Measure;
import ch.hearc.medicalcheck.service.MeasureService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Controller to manage all request
 *  for the measure ressources
 */

@RestController
@RequestMapping("/measures")
public class MeasureController {
	@Autowired
	MeasureService measureService;

	/**
	 * list all measure
	 */
	@GetMapping("")
	public List<Measure> getAll() {
		return measureService.getAll();
	}
	
	/**
	 * get a measure by id
	 */
	@GetMapping("{id}")
	public Measure getMeasure(@PathVariable int id) {
		return measureService.get(id);
	}
	
	/**
	 * list all measure for a user
	 */
	@GetMapping("/user/{iduser}")
	public List<Measure> getAllByIdUser(@PathVariable int iduser) {
		return measureService.getAllByIdUser(iduser);
	}
	
	/**
	 * get last measure for a user
	 */
	@GetMapping("/user/last/{iduser}")
	public Measure getLastByIdUser(@PathVariable int iduser) {
		return measureService.getLastByIdUser(iduser);
	}
	
	/**
	 * list all measure for a user by hour (avg) for a date 
	 */
	@GetMapping("/user/average/{iduser}/{date}")
	public Map<Integer, Double> getMapAverage(@PathVariable Integer iduser, @PathVariable String date) {
		return measureService.getMapAverage(iduser, date);
	}
	
	/**
	 * update a measure
	 */
	@PutMapping("{id}")
	public Measure update(@PathVariable int id,@RequestBody Measure newMeasure) {
		return measureService.update(id,newMeasure);
	}

	/**
	 * create a measure
	 */
	@PostMapping("")
	public Measure create(@RequestBody Measure newMeasure) {
		return measureService.create(newMeasure);
	}
	

	/**
	 * delete a measure
	 */
	@DeleteMapping("{id}")
	public void delete(@PathVariable int id) {
		measureService.delete(id);
	}

}
