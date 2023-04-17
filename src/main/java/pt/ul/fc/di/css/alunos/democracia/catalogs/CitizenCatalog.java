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
    return citizenRepository.findCitizenByNif(nif);
  }

  public Delegate getDelegate(int nif) {
    return citizenRepository.findDelegateByNif(nif);
  }

  public List<Delegate> getDelegates() {
    return citizenRepository.getAllDelegates();
  }

  // Ideia para o caso de uso J -> 1 - d = getDelegate | 2 - if(d=null) c = getCitizen, NotDelegate
  // = true
  // Mais eficiente que usar o isDelegate já q no best case scenario só percorre uma tabela
  public boolean isDelegate(int nif) {
    Delegate d = citizenRepository.findDelegateByNif(nif);
    return d != null;
  }
}
