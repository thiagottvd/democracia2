package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;
import pt.ul.fc.di.css.alunos.democracia.repositories.DelegateThemeRepository;

public class DelegateThemeCatalog {
  private final DelegateThemeRepository dtRepository;

  public DelegateThemeCatalog(DelegateThemeRepository dtRepo) {
    this.dtRepository = dtRepo;
  }

  public List<DelegateTheme> getAll() {
    return dtRepository.getAllDTs();
  }

  public void addDT(DelegateTheme dt) {
    dtRepository.save(dt);
  }
}
