package pt.ul.fc.di.css.alunos.democracia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableScheduling
public class DemocraciaApp {

  private static final Logger log = LoggerFactory.getLogger(DemocraciaApp.class);

  public static void main(String[] args) {
    SpringApplication.run(DemocraciaApp.class, args);
  }

  @Bean
  public CommandLineRunner demo() {
    return (args) -> {};
  }
}
