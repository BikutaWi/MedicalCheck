package ch.hearc.medicalcheck.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.medicalcheck.model.Follow;
import ch.hearc.medicalcheck.repository.FollowRepository;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * bridge between repository and controller
 * each methods is explained in the associate controller
 * check them for more information
 */

@Service
public class FollowService {
	@Autowired
	private FollowRepository followRepository;

	public List<Follow> getAll() {
		return followRepository.findAll();
	}
	
	public List<Follow> getAllFollowByIdUserCareKeeper(Integer idUserCareKeeper) {
		return followRepository.getAllFollowByIdUserCareKeeper(idUserCareKeeper);
	}
	
	public List<Follow> getAllFollowByIdUserPatient(Integer idUserPatient) {
		return followRepository.getAllFollowByIdUserPatient(idUserPatient);
	}


	public Follow get(Integer id) {
		return followRepository.findById(id).get();
	}
	
	public Follow create(Follow follow) {
		return followRepository.save(follow);
	}

	public Follow update(Integer idFollow, Follow follow) {
		Optional<Follow> followData = followRepository.findById(follow.getId());

		if (followData.isPresent()) {
			follow.setId(idFollow);
			return followRepository.save(follow);
		}
		return null; 
	}

	public void delete(Integer id) {
		followRepository.deleteById(id);
	}
}
