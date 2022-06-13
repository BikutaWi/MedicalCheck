package ch.hearc.medicalcheck.service;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import ch.hearc.medicalcheck.model.User;
import ch.hearc.medicalcheck.repository.UserRepository;

/**
 * bridge between repository and controller
 * each methods is explained in the associate controller
 * check them for more information
 */

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public User get(Integer id) {
		return userRepository.findById(id).get();
	}

	public User get(String username) {
		return userRepository.getByUserName(username);
	}

	public List<User> getAllAvailableCarekeeper(int iduser) {
		return userRepository.getAllAvailableCarekeeper(iduser);
	}

	public List<User> getAllMyCarekeeper(int iduser) {
		return userRepository.getAllMyCarekeeper(iduser);
	}

	public User create(User user) {
		return userRepository.save(user);
	}

	public User update(Integer idTraitement, User user) {
		Optional<User> userData = userRepository.findById(user.getId());

		if (userData.isPresent()) {
			user.setId(idTraitement);
			return userRepository.save(user);
		}
		return null;
	}

	public void delete(Integer id) {
		userRepository.deleteById(id);
	}
}
