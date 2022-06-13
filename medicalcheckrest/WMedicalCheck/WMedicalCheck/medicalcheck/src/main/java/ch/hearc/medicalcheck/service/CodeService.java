package ch.hearc.medicalcheck.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.medicalcheck.model.Code;
import ch.hearc.medicalcheck.model.Follow;
import ch.hearc.medicalcheck.repository.CodeRepository;
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
public class CodeService {
	
	@Autowired
	private CodeRepository codeRepository;

	public List<Code> getAll() {
		return codeRepository.findAll();
	}
	

	public Code get(Integer id) {
		return codeRepository.findById(id).get();
	}
	
	public Code getByCode(Integer code) {
		return codeRepository.findByCode(code);
	}
	
	public Code create(Code code) {
		return codeRepository.save(code);
	}
	

	public void delete(Integer id) {
		codeRepository.deleteById(id);
	}
}
