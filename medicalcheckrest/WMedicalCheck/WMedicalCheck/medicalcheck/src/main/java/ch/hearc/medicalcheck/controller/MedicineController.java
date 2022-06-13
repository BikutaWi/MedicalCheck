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

import ch.hearc.medicalcheck.model.Medicine;
import ch.hearc.medicalcheck.service.MedicineService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Controller to manage all request
 *  for the medicine ressources
 */
@RestController
@RequestMapping("/medicines")
public class MedicineController {
	@Autowired
	MedicineService medicineService;

	/**
	 * list all medicine
	 */
	@GetMapping("")
	public List<Medicine> getAll() {
		return medicineService.getAll();
	}
	
	/**
	 * list a medicine by id
	 */
	@GetMapping("{id}")
	public Medicine getMedicine(@PathVariable int id) {
		return medicineService.get(id);
	}
	
	/**
	 * list all medicine for a user
	 */
	@GetMapping("/user/{iduser}")
	public List<Medicine> getAllByIdUser(@PathVariable int iduser) {
		return medicineService.getAllByIdUser(iduser);
	}
	
	/**
	* update a medicine
	*/
	@PutMapping("{id}")
	public Medicine update(@PathVariable int id,@RequestBody Medicine newMedicine) {
		return medicineService.update(id,newMedicine);
	}

	/**
	 * create a medicine
	 */
	@PostMapping("")
	public Medicine create(@RequestBody Medicine newMedicine) {
		return medicineService.create(newMedicine);
	}

	/**
	 * delete a medicine
	 */
	@DeleteMapping("{id}")
	public void delete(@PathVariable int id) {
		medicineService.delete(id);
	}

}
