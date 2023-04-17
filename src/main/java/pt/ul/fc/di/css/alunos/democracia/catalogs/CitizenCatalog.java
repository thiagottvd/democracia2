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
   * Constructor that receives the CitizenRepository dependency.
   *
   * @param citizenRepository the repository used to access Citizen data.
   */
  @Autowired
  public CitizenCatalog(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }

  public Delegate getDelegate(int cc) {
    return citizenRepository.findDelegateByCc(cc);
  }

  public List<Delegate> getDelegates() {
    return citizenRepository.getAllDelegates();
  }

  // Ideia para o caso de uso J -> 1 - d = getDelegate | 2 - if(d=null) c = getCitizen, NotDelegate
  // = true
  // Mais eficiente que usar o isDelegate já q no best case scenario só percorre uma tabela
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
}
