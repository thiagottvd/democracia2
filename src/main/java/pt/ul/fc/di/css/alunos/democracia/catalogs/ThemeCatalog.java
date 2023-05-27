package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;

/**
 * The ThemeCatalog class is responsible for managing themes by providing operations to retrieve,
 * save and close them. It uses a ThemeRepository to perform the database operations.
 */
@Component
public class ThemeCatalog {
  private final ThemeRepository themeRepository;

  /**
   * Constructs a ThemeCatalog instance with the specified ThemeRepository instance.
   *
   * @param themeRepository the ThemeRepository instance to use for performing database operations.
   */
  public ThemeCatalog(ThemeRepository themeRepository) {
    this.themeRepository = themeRepository;
  }

  /**
   * Return a list of all themes.
   *
   * @return a list of all themes.
   */
  public List<Theme> getThemes() {
    return themeRepository.getAllThemes();
  }

  /*
   * Returns a theme given its designation.
   *
   * @param designation The theme designation.
   * @return an Optional containing the theme if found, otherwise an empty Optional.
   */
  public Optional<Theme> getTheme(String designation) {
    return themeRepository.findThemeByDesignation(designation);
  }
}
