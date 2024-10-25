package com.sb_elastic.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(basePackages = "com.sb_elastic.poc.storage.repositories")
@SpringBootApplication
public class SbElasticPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbElasticPocApplication.class, args);
	}

}
