package github.maxsplawski.realworld.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"github.maxsplawski.realworld.domain.*"})
public class PersistenceConfiguration {
}
