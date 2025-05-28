package com.dongdong.nameSolver;

import com.dongdong.nameSolver.test.TestDataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication(scanBasePackages = {"com.dongdong.nameSolver.domain", "com.dongdong.nameSolver.global"})
@Import(TestDataInitializer.class)
@EnableScheduling
@EnableAsync
public class NameSolverApplication {

	public static void main(String[] args) {
		SpringApplication.run(NameSolverApplication.class, args);
	}
}
