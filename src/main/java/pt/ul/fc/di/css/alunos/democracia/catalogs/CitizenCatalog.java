package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;

/**
 * The CitizenCatalog class is responsible for managing citizens by providing operations to
 * retrieve, save and close them. It uses a CitizenRepository to perform the database operations.
 */
@Component
public class CitizenCatalog {

  private final CitizenRepository citizenRepository;

  /**
   * Constructor of the CitizenCatalog class that receives the CitizenRepository dependency.
   *
   * @param citizenRepository the repository used to access Citizen data.
   */
  @Autowired
  public CitizenCatalog(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }

  /**
   * Finds a Delegate from the database with the corresponding cc.
   * @param cc The cc of the Delegate we want to retrieve.
   * @return The Delegate if found.
   */
  public Delegate getDelegate(int cc) {
    return citizenRepository.findDelegateByCc(cc);
  }

  /**
   * Gets a list of all the Delegates in the database.
   * @return The list containing all Delegates.
   */
  public List<Delegate> getDelegates() {
    return citizenRepository.getAllDelegates();
  }

  /**
   * Checks if a Delegate with the given cc exists.
   * @param cc The cc of the Citizen we want to check.
   * @return True or false accordingly.
   */
  public boolean isDelegate(int cc) {
    Delegate d = citizenRepository.findDelegateByCc(cc);
    return d != null;
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

  public List<Citizen> findAllNonVoters(List<Citizen> voters) {
    return citizenRepository.findAllNonVoters(voters);
  }
}
