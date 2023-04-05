package travellin.travelblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("travellin.travelblog.repositories")
public class TravelBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelBlogApplication.class, args);
	}

}
