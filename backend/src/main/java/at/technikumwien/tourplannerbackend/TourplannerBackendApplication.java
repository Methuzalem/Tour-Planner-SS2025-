package at.technikumwien.tourplannerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TourplannerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourplannerBackendApplication.class, args);
	}
}

