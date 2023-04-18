package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;
import pt.ul.fc.di.css.alunos.democracia.repositories.DelegateThemeRepository;

/**
 * The DelegateTheme class is responsible for managing DelegateThemes by providing operations to
 * retrieve, save and close them. It uses a DelegateThemeRepository to perform the database operations.
 */
@Component
public class DelegateThemeCatalog {
  private final DelegateThemeRepository dtRepository;

  /**
   * Constructor of the DelegateThemeCatalog class that receives the CitizenRepository dependency.
   * @param dtRepo the repository used to access DelegateTheme data.
   */
  public DelegateThemeCatalog(DelegateThemeRepository dtRepo) {
    this.dtRepository = dtRepo;
  }

  /**
   * Gets a list of all the DelegateThemes in the database.
   * @return The list containing all DelegateThemes.
   */
  public List<DelegateTheme> getAll() {
    return dtRepository.getAllDTs();
  }

  /**
   * Adds a new DelegateTheme to the database.
   * @param dt The DelegateTheme we want to add.
   */
  public void addDT(DelegateTheme dt) {
    dtRepository.save(dt);
  }
}
