package ch.hearc.medicalcheck.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.medicalcheck.model.Planning;
import ch.hearc.medicalcheck.repository.PlanningRepository;

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
public class PlanningService {
	@Autowired
	private PlanningRepository planningRepository;

	public List<Planning> getAll() {
		return planningRepository.findAll();
	}
	
	public List<Planning> getAllByIdMedicine(Integer idmedicine) {
		return planningRepository.getAllPlanningByIdMedicine(idmedicine);
	}
	
	public List<Planning> getAllTodayByIdUser(Integer iduser, String day) {
		return planningRepository.getAllPlanningTodayByIdUser(iduser, day);
	}

	public List<Planning> getAllToday(String day) {
		return planningRepository.getAllPlanningToday(day);
	}
	
	public Planning get(Integer id) {
		return planningRepository.findById(id).get();
	}
	
	public Planning create(Planning planning) {
		return planningRepository.save(planning);
	}

	public Planning update(Integer idPlanning, Planning planning) {
		Optional<Planning> planningData = planningRepository.findById(planning.getId());

		if (planningData.isPresent()) {
			planning.setId(idPlanning);
			return planningRepository.save(planning);
		}
		return null; 
	}

	public void delete(Integer id) {
		planningRepository.deleteById(id);
	}
}
