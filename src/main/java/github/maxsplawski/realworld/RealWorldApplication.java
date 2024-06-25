package github.maxsplawski.realworld;

import github.maxsplawski.realworld.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"github.maxsplawski.realworld.*"})
@EnableConfigurationProperties(RsaKeyProperties.class)
public class RealWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealWorldApplication.class, args);
    }

}
