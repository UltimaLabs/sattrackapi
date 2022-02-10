package com.ultimalabs.sattrackapi;

import com.ultimalabs.sattrackapi.predict.service.PredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SattrackapiApplication implements CommandLineRunner {
	@Autowired private PredictService service;
	public static void main(String[] args) {
		SpringApplication.run(SattrackapiApplication.class, args);
	}

	@Override
	@Profile("development")
	public void run(String... args) throws Exception {
		System.out.println("In development profile!");
		/*SatellitePass pass = service.getNextEventWithoutDetails("25544", 15.9819, 45.815, 400, 1);
		SatellitePass passWithDetails = service.getNextEventWithDetails("25544", 15.9819, 45.815, 400, 1, 1);
		if(pass != null)
			System.out.println(pass.toString());
		if(passWithDetails != null)
			System.out.println(passWithDetails.toString());
		List<SatellitePass> passes = service.getNextEventsWithoutDetails(5, "25544", 15.9819, 45.815, 400, 1);
		if(passes != null)
			for (SatellitePass pass : passes)
				System.out.println(pass.getRisePoint().getT());*/
	}
}
