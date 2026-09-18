package mpack;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"controllers" , "mpack" , "services","components","configs","api","advice"})
@EnableJpaRepositories("repositories")
@EntityScan("entities")
public class EduSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduSyncApplication.class, args);
	}

}
