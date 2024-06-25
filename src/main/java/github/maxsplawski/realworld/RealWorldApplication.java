package github.maxsplawski.realworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"github.maxsplawski.realworld.*"})
public class RealWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealWorldApplication.class, args);
    }

}
