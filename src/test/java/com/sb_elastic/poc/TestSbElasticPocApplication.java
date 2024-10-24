package com.sb_elastic.poc;

import org.springframework.boot.SpringApplication;

public class TestSbElasticPocApplication {

	public static void main(String[] args) {
		SpringApplication.from(SbElasticPocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
