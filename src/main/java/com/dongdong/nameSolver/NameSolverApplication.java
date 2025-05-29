package com.dongdong.nameSolver;

import com.dongdong.nameSolver.test.TestDataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.dongdong.nameSolver.domain", "com.dongdong.nameSolver.global"})
@Import(TestDataInitializer.class)
@EnableScheduling
@EnableAsync
@ConfigurationPropertiesScan
public class NameSolverApplication {

	public static void main(String[] args) {
		SpringApplication.run(NameSolverApplication.class, args);
	}
}
