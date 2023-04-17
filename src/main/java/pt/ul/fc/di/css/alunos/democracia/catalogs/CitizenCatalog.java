package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;

/**
 * The CitizenCatalog class is responsible for managing citizens by providing operations to
 * retrieve, save and close them. It uses a CitizenRepository to perform the database operations.
 */
@Component
public class CitizenCatalog {

  private final CitizenRepository citizenRepository;

  /**
   * Constructor that receives the CitizenRepository dependency.
   *
   * @param citizenRepository the repository used to access Citizen data.
   */
  @Autowired
  public CitizenCatalog(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }

  /**
   * Retrieves a Citizen object from the database based on the provided cc number.
   *
   * @param cc the cc number of the citizen to retrieve.
   * @return an Optional containing the Citizen object if found, otherwise an empty Optional.
   */
  public Optional<Citizen> getCitizenByCc(int cc) {
    return citizenRepository.findByCc(cc);
  }
}
