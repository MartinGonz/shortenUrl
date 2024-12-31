package com.example.shortUrl;

import com.example.shortUrl.model.UrlMapping;
import com.example.shortUrl.repository.UrlRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShortUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortUrlApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(UrlRepository repository) {
		return args -> {
			System.out.println("Saving URL to Cassandra" + repository);
			repository.save(new UrlMapping("test123", "http://example.com", "user123"));
		};
	}
}
