package ch.hearc.medicalcheck.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.medicalcheck.model.Measure;
import ch.hearc.medicalcheck.repository.MeasureRepository;

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
public class MeasureService {
	@Autowired
	private MeasureRepository measureRepository;

	public List<Measure> getAll() {
		return measureRepository.findAll();
	}

	public Measure get(Integer id) {
		return measureRepository.findById(id).get();
	}

	public List<Measure> getAllByIdUser(Integer iduser) {
		return measureRepository.getAllMeasureByIdUser(iduser);
	}
	
	public Measure getLastByIdUser(Integer iduser) {
		return measureRepository.getLastMeasureByIdUser(iduser);
	}
	
	public Map<Integer, Double> getMapAverage(Integer iduser, String date) {
		
		List<Tuple> list = measureRepository.getMapAverageMeasure(iduser, date);
		return list.stream().collect(Collectors.toMap(t -> (Integer)t.get(0), t -> (Double)t.get(1)));
	}

	public Measure create(Measure measure) {
		return measureRepository.save(measure);
	}

	public Measure update(Integer idMeasure, Measure measure) {
		Optional<Measure> measureData = measureRepository.findById(measure.getId());

		if (measureData.isPresent()) {
			measure.setId(idMeasure);
			return measureRepository.save(measure);
		}
		return null; 
	}

	public void delete(Integer id) {
		measureRepository.deleteById(id);
	}
}
