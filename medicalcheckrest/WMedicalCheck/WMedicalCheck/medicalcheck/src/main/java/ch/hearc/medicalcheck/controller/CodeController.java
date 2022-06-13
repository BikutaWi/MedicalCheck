package ch.hearc.medicalcheck.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.medicalcheck.model.Code;
import ch.hearc.medicalcheck.model.User;
import ch.hearc.medicalcheck.service.CodeService;
import ch.hearc.medicalcheck.service.UserService;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Controller to manage all request
 *  for the code ressources
 */

@RestController
@RequestMapping("/codes")
public class CodeController {
	@Autowired
	CodeService codeService;

	@Autowired
	UserService userService;

	/**
	 * generate a code for a user to login on the watch
	 * @param id iduser for whom the code has to be generated
	 * @return the code 
	 */
	@GetMapping("/generate/{id}")
	public Integer generateCode(@PathVariable int id) {

		//retrieve the user 
		User user = userService.get(id);

		//[min,max] for the code
		int min = 100000;
		int max = 999999;

		// Generate random int value from min to max, until the code is not in the database
		int random_int;

		do
		{
			random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		}while(codeService.getByCode(random_int) != null);
		
		//create the code
		Code newCode = new Code();
		newCode.setCode(random_int);
		newCode.setUser(user);


		//store
		Code code = codeService.create(newCode);
		
		//delete code async after 30s
		CompletableFuture.runAsync(() -> delete(code));

		//return the code to the response
		return code.getCode();
	}

	@GetMapping("/user/{code}")
	/**
	 * check if the code is correct
	 * @param code check if the code exists
	 * @return the user which is associate to the code
	 */
	public User getUserByCode(@PathVariable int code) {
		
		Code c = codeService.getByCode(code);
		
		if(c != null)
			return codeService.getByCode(code).getUser();
		else
			return null;
	}
	/**
	 * delete a code after 30s
	 * @param code to delete
	 */
	@Async
	private void delete(Code code) {
		try {
	        TimeUnit.SECONDS.sleep(30); //sleep 30s
			codeService.delete(code.getId()); //delete the code
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 

	}
	
	

}
