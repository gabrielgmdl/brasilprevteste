package br.com.brasilprevteste.config;

import br.com.brasilprevteste.model.audit.AuditorAwareImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {

	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}

//	@Bean
//	public BasicDataSource dataSource() throws URISyntaxException {
//		URI dbUri = new URI(System.getenv("DATABASE_URL"));
//
//		String username = dbUri.getUserInfo().split(":")[0];
//		String password = dbUri.getUserInfo().split(":")[1];
//		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
//
//		BasicDataSource basicDataSource = new BasicDataSource();
//		basicDataSource.setUrl(dbUrl);
//		basicDataSource.setUsername(username);
//		basicDataSource.setPassword(password);
//
//		return basicDataSource;
//	}
}