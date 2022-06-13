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

import ch.hearc.medicalcheck.model.Traitement;
import ch.hearc.medicalcheck.service.TraitementService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Controller to manage all request
 *  for the traitement ressources
 */
@RestController
@RequestMapping("/traitements")
public class TraitementController {
	@Autowired
	TraitementService traitementService;

	/**
	 * list all traitements
	 */
	@GetMapping("")
	public List<Traitement> getAll() {
		return traitementService.getAll();
	}
	
	/**
	 * list all traitements by idplanning
	 */
	@GetMapping("/planning/{idplanning}")
	public List<Traitement> getAllByIdPlanning(@PathVariable int idplanning) {
		return traitementService.getAllByIdPlanning(idplanning);
	}
	
	/**
	 * list all traitements for a user
	 */
	@GetMapping("/user/{iduser}")
	public List<Traitement> getAllByIdUser(@PathVariable int iduser) {
		return traitementService.getAllByIdUser(iduser);
	}
	
	/**
	 * count all traitements for a user
	 */
	@GetMapping("/count/{iduser}")
	public Integer countTreatmentNotTaken(@PathVariable int iduser) {
		return traitementService.countTreatmentNotTaken(iduser);
	}

	/**
	 * get a traitement by id
	 */
	@GetMapping("{id}")
	public Traitement get(@PathVariable int id) {
		return traitementService.get(id);
	}

	/**
	 * update a traitement by id
	 */
	@PutMapping("{id}")
	public Traitement update(@PathVariable int id, @RequestBody Traitement traitement) {	
		return traitementService.update(id, traitement);
	}

	/**
	 * create a traitement
	 */
	@PostMapping("")
	public Traitement create(@RequestBody Traitement traitement) {
		return traitementService.create(traitement);
	}

	/**
	 * delete a traitement
	 */
	@DeleteMapping("{id}")
	public void delete(@PathVariable int id) {
		traitementService.delete(id);
	}

}
