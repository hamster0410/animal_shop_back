package animal_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Animal_ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(Animal_ShopApplication.class, args  );
	}

}
