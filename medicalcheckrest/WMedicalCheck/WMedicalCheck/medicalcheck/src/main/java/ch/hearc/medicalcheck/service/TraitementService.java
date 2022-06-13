package ch.hearc.medicalcheck.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.medicalcheck.model.Traitement;
import ch.hearc.medicalcheck.repository.TraitementRepository;

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
public class TraitementService {
	@Autowired
	private TraitementRepository traitementRepository;

	public List<Traitement> getAll() {
		return traitementRepository.findAll();
	}
	
	public List<Traitement> getAllByIdPlanning(Integer idtraitement) {
		return traitementRepository.getAllTreatmentByIdPlanning(idtraitement);
	}
	
	public List<Traitement> getAllByIdUser(Integer iduser) {
		return traitementRepository.getAllTreatmentByIdUser(iduser);
	}
	
	public Integer countTreatmentNotTaken(Integer iduser) {
		return traitementRepository.countTreatmentNotTaken(iduser);
	}

	public Traitement get(Integer id) {
		return traitementRepository.findById(id).get();
	}
	
	public Traitement create(Traitement traitement) {
		return traitementRepository.save(traitement);
	}

	public Traitement update(Integer idTraitement, Traitement traitement) {
		Optional<Traitement> traitementData = traitementRepository.findById(traitement.getId());

		if (traitementData.isPresent()) {
			traitement.setId(idTraitement);
			return traitementRepository.save(traitement);
		}
		return null; 
	}

	public void delete(Integer id) {
		traitementRepository.deleteById(id);
	}
}
