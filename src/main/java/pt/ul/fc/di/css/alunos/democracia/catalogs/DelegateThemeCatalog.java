package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.repositories.DelegateThemeRepository;

/**
 * The DelegateThemeCatalog class is responsible for managing delegate-theme associations by
 * providing operations to retrieve, save and close them. It uses a DelegateThemeRepository to
 * perform the database operations.
 */
@Component
public class DelegateThemeCatalog {
  private final DelegateThemeRepository dtRepository;

  /**
   * Constructs a DelegateThemeCatalog instance with the specified DelegateThemeRepository instance.
   *
   * @param dtRepository the DelegateThemeRepository instance to use for performing database
   *     operations.
   */
  public DelegateThemeCatalog(DelegateThemeRepository dtRepository) {
    this.dtRepository = dtRepository;
  }

  /**
   * Returns a list of all DelegateThem objects.
   *
   * @return a list of all DelegateThem objects.
   */
  public List<DelegateTheme> getAll() {
    return dtRepository.getAllDTs();
  }

  /**
   * Saves a DelegateTheme object into the database.
   *
   * @param dt The DelegateThem object to save.
   */
  public void saveDelegateTheme(DelegateTheme dt) {
    dtRepository.save(dt);
  }

  /**
   * Deletes a DelegateTheme object from the database.
   *
   * @param delegateTheme The DelegateThem object to delete.
   */
  public void deleteDelegateTheme(DelegateTheme delegateTheme) {
    dtRepository.delete(delegateTheme);
  }

  /**
   * Checks if a DelegateTheme object exists for the given delegate and theme.
   *
   * @param delegate The delegate to check.
   * @param theme The theme to check.
   * @return true if a delegate theme exists for the given delegate and theme, false otherwise.
   */
  public boolean delegateThemeExists(Delegate delegate, Theme theme) {
    return dtRepository.findByDelegateAndTheme(delegate, theme).isPresent();
  }

  /**
   * Retrieves the DelegateTheme object for the given delegate and theme.
   *
   * @param delegate The delegate to retrieve the theme for.
   * @param theme The theme to retrieve the delegate for.
   * @return An Optional containing the delegate theme if found, or an empty Optional if not found.
   */
  public Optional<DelegateTheme> getDtByDelegateAndTheme(Delegate delegate, Theme theme) {
    return dtRepository.findByDelegateAndTheme(delegate, theme);
  }
}
