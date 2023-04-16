package pt.ul.fc.di.css.alunos.democracia.catalogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;

@Component
public class CitizenCatalog {

  private final CitizenRepository citizenRepository;

  @Autowired
  public CitizenCatalog(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }
}
