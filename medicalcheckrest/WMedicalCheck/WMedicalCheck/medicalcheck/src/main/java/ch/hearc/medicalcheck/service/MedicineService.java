package ch.hearc.medicalcheck.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.medicalcheck.model.Medicine;
import ch.hearc.medicalcheck.repository.MedicineRepository;

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
public class MedicineService {
	@Autowired
	private MedicineRepository medicineRepository;

	public List<Medicine> getAll() {
		return medicineRepository.findAll();
	}

	public Medicine get(Integer id) {
		return medicineRepository.findById(id).get();
	}

	public List<Medicine> getAllByIdUser(Integer iduser) {
		return medicineRepository.getAllMedicineByIdUser(iduser);
	}

	public Medicine create(Medicine medicine) {
		return medicineRepository.save(medicine);
	}

	public Medicine update(Integer idMedicine, Medicine medicine) {
		Optional<Medicine> medicineData = medicineRepository.findById(medicine.getId());

		if (medicineData.isPresent()) {
			medicine.setId(idMedicine);
			return medicineRepository.save(medicine);
		}
		return null; 
	}

	public void delete(Integer id) {
		medicineRepository.deleteById(id);
	}
}
