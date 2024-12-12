package animal_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "animal_shop",
		excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "animal_shop\\.tools\\.chat\\..*") // MongoDB 패키지 제외
)
@EnableMongoRepositories(basePackages = "animal_shop.tools.chat")
public class Animal_ShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(Animal_ShopApplication.class, args);
	}

}
