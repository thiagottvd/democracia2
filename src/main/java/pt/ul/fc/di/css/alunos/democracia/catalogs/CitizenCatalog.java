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
   * Constructs a CitizenCatalog instance with the specified CitizenRepository instance.
   *
   * @param citizenRepository the CitizenRepository instance to use for performing database
   *     operations.
   */
  @Autowired
  public CitizenCatalog(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }

  /**
   * Returns an Optional containing a delegate given their citizen card number.
   *
   * @param citizenCardNumber the citizen card number of the delegate to retrieve.
   * @return an Optional containing the delegate object if found, otherwise an empty Optional.
   */
  public Optional<Delegate> getDelegateByCitizenCardNumber(Integer citizenCardNumber) {
    return citizenRepository.findDelegateByCitizenCardNumber(citizenCardNumber);
  }

  /**
   * Returns a List of all delegates.
   *
   * @return a List of all delegates, or an empty List if no delegates are found
   */
  public List<Delegate> getDelegates() {
    return citizenRepository.findAllDelegates();
  }

  /**
   * Retrieves a Citizen object from the database based on the provided citizen card number.
   *
   * @param citizenCardNumber the citizen card number of the citizen to retrieve.
   * @return an Optional containing the Citizen object if found, otherwise an empty Optional.
   */
  public Optional<Citizen> getCitizenByCitizenCardNumber(Integer citizenCardNumber) {
    return citizenRepository.findByCitizenCardNumber(citizenCardNumber);
  }

  /**
   * Retrieves a list of all Citizen objects that did not vote in a poll.
   *
   * @param voters a list of Citizen objects that voted in a poll.
   * @return a list of all Citizen objects that did not vote in a poll.
   */
  public List<Citizen> findAllNonVoters(List<Citizen> voters) {
    return citizenRepository.findAllNonVoters(voters);
  }
}
