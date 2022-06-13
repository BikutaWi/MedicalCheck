package ch.hearc.medicalcheck.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.medicalcheck.model.User;
import ch.hearc.medicalcheck.service.UserService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Controller to manage all request
 *  for the user ressources
 */

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserService userService;

	/**
	 * list all users
	 */
	@GetMapping("")
	public List<User> getAll() {
		return userService.getAll();
	}
	
	
	/**
	 * get a user by id
	 */
	@GetMapping("{id}")
	public User get(@PathVariable int id) {
		return userService.get(id);
	}
	
	/**
	 * get all user which are carekeeper
	 */
	@GetMapping("/carekeeper/{iduser}")
	public List<User> getAllAvailableCarekeeper(@PathVariable int iduser) {
		return userService.getAllAvailableCarekeeper(iduser);
	}
	
	/**
	 * get a user by username
	 */
	@GetMapping("/login/{username}")
	public User get(@PathVariable String username) {
		return userService.get(username);
	}
	
	
	/**
	 * update a user by id
	 */
	@PutMapping("{id}")
	public User update(@PathVariable int id, @RequestBody User user) {
		return userService.update(id, user);
	}

	/**
	 * create a user
	 */
	@PostMapping("")
	public User create(@RequestBody User user) {
		return userService.create(user);
	}

	/**
	 * delete a user
	 */
	@DeleteMapping("{id}")
	public void delete(@PathVariable int id) {
		userService.delete(id);
	}
}
