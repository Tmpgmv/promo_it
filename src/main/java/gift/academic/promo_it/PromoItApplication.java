package gift.academic.promo_it;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PromoItApplication {
	@Value("${spring.application.debug:false}")
	private static boolean debug;

	public static void main(String[] args) {

		SpringApplication.run(PromoItApplication.class, args);
	}

}
