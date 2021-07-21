package br.com.brasilprevteste;

import br.com.brasilprevteste.config.property.BrasilprevtesteProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(BrasilprevtesteProperty.class)
public class BrasilprevtesteApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BrasilprevtesteApplication.class, args);
	}

}
