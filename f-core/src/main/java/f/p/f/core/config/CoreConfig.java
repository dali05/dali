package f.p.f.core.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"f.p.f.core"})
@EntityScan(basePackages = {"f.p.f.core"})
@ComponentScan(basePackages = {"f.p.f.core.domain", "f.p.f.core.repository"})
public class CoreConfig {

    public static void main(String[] args) {
        SpringApplication.run(CoreConfig.class, args);
    }

}
