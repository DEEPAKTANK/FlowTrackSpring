package com.deepak.proexpenditure.pro_expenditure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")  // Enable auditing
public class ProExpenditureApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProExpenditureApplication.class, args);
	}


}

