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

import ch.hearc.medicalcheck.model.Planning;
import ch.hearc.medicalcheck.service.PlanningService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Controller to manage all request
 *  for the planning ressources
 */
@RestController
@RequestMapping("/plannings")
public class PlanningController {
	@Autowired
	PlanningService planningService;

	/**
	 *  list all planning
	 */
	@GetMapping("")
	public List<Planning> getAll() {
		return planningService.getAll();
	}
	
	/**
	 * list all planning by medicine
	 */
	@GetMapping("/medicine/{idmedicine}")
	public List<Planning> getAllByIdMedicine(@PathVariable int idmedicine) {
		return planningService.getAllByIdMedicine(idmedicine);
	}
	
	/**
	 * list all planning for a day by user
	 */
	@GetMapping("/today/{iduser}/{day}")
	public List<Planning> getAllTodayByIdUser(@PathVariable int iduser, @PathVariable String day) {
		return planningService.getAllTodayByIdUser(iduser, day);
	}
	
	/**
	 * list all planning for a day
	 */
	@GetMapping("/today/{day}")
	public List<Planning> getAllToday(@PathVariable String day) {
		return planningService.getAllToday(day);
	}

	/**
	 * get a planning by id
	 */
	@GetMapping("{id}")
	public Planning get(@PathVariable int id) {
		return planningService.get(id);
	}

	/**
	 *  update a planning by id
	 */
	@PutMapping("{id}")
	public Planning update(@PathVariable int id, @RequestBody Planning planning) {
		return planningService.update(id, planning);
	}

	/**
	 * create a planning
	 */
	@PostMapping("")
	public Planning create(@RequestBody Planning planning) {
		return planningService.create(planning);
	}

	/**
	 * delete a planning by id
	 */
	@DeleteMapping("{id}")
	public void delete(@PathVariable int id) {
		planningService.delete(id);
	}

}
