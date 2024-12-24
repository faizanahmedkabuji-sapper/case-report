package ai.sapper.casereport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CaseReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaseReportApplication.class, args);
	}

}
