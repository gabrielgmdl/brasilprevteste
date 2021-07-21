package br.com.brasilprevteste.config;

import br.com.brasilprevteste.config.property.BrasilprevtesteProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class WebConfig {

	@Autowired
	private BrasilprevtesteProperty property;
}
