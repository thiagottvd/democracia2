package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;

@Component
public class CitizenCatalog {
  private final CitizenRepository citizenRepository;

  @Autowired
  public CitizenCatalog(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }

  public Citizen getCitizen(int nif) {
    // TODO verifications
    return citizenRepository.findCitizenByNif(nif);
  }

  public Delegate getDelegate(int nif) {
    // TODO verifications e perceber se Delegates vao ter tabela propria ou nao
    return citizenRepository.findDelegateByNif(nif);
  }

  public List<Delegate> getDelegates() {
    return citizenRepository.getAllDelegates();
  }
}
