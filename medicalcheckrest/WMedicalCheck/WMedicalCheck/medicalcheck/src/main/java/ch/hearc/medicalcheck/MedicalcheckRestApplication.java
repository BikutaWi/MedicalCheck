package ch.hearc.medicalcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

@SpringBootApplication
@EnableScheduling
public class MedicalcheckRestApplication {

	public static void main(String[] args) {
		//enter point for the application
		SpringApplication.run(MedicalcheckRestApplication.class, args);
	}

}
