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

import ch.hearc.medicalcheck.model.Follow;
import ch.hearc.medicalcheck.service.FollowService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Controller to manage all request
 *  for the follow ressources
 */
@RestController
@RequestMapping("/follows")
public class FollowController {
	@Autowired
	FollowService followService;

	/**
	 * list all the follow relationship
	 */
	@GetMapping("")
	public List<Follow> getAll() {
		return followService.getAll();
	}
	
	/**
	 * list all relationship that a carekeeper follow
	 */
	@GetMapping("/usercarekeeper/{idUserCareKeeper}")
	public List<Follow> getAllFollowByIdUserCareKeeper(@PathVariable int idUserCareKeeper) {
		return followService.getAllFollowByIdUserCareKeeper(idUserCareKeeper);
	}
	
	/**
	 * list all relationship that is follow by a carekeeper
	 */
	@GetMapping("/userpatient/{idUserPatient}")
	public List<Follow> getAllFollowByIdUserPatient(@PathVariable int idUserPatient) {
		return followService.getAllFollowByIdUserPatient(idUserPatient);
	}

	/**
	 *  get a follow by id
	 */
	@GetMapping("{id}")
	public Follow get(@PathVariable int id) {
		return followService.get(id);
	}

	/**
	 *  update a follow by id
	 */
	@PutMapping("{id}")
	public Follow update(@PathVariable int id, @RequestBody Follow follow) {
		return followService.update(id, follow);
	}

	/**
	 *  create a follow
	 */
	@PostMapping("")
	public Follow create(@RequestBody Follow follow) {
		return followService.create(follow);
	}

	/**
	 *  delete a follow
	 */
	@DeleteMapping("{id}")
	public void delete(@PathVariable int id) {
		followService.delete(id);
	}

}
