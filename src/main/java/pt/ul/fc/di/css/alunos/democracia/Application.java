package pt.ul.fc.di.css.alunos.democracia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pt.ul.fc.di.css.alunos.democracia.entities.*;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;

@SpringBootApplication
public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner demo(
      BillRepository
          billRepository) { // , CitizenRepository billRepository, BillRepository billRepository
    return (args) -> {
      // Create temporary files
      //      File tempFile = File.createTempFile("test", ".pdf");
      //      FileWriter writer = new FileWriter(tempFile);
      //      writer.write("Hello, world!");
      //      writer.close();
      //      byte[] fileData = Files.readAllBytes(tempFile.toPath());
      //
      //      Delegate delegate = new Delegate("Rodrigo T.", 123);
      //      entityManager.persist(delegate);
      //
      //      Theme theme = new Theme("Saude", null);
      //      entityManager.persist(theme);
      //
      //      Bill expectedBill =
      //              new Bill(
      //                      "Compra vacinas covid",
      //                      "Compra de vacinas pfizer",
      //                      fileData,
      //                      LocalDate.now().plusDays(1),
      //                      delegate,
      //                      theme);
      //      entityManager.persist(expectedBill);
      //
      //      BillDTO expectedBillDTO = new BillDTO(expectedBill);
      //
      //      BillDTO actualBill = actualBill =
      // consultBillsService.getBillDetails(expectedBill.getId());
    };
  }
}
