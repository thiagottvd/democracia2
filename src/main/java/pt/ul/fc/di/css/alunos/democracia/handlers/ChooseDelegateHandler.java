package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.DelegateThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.DelegateDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ThemeNotFoundException;

/**
 * Handles use case I.
 *
 * <p>Handler that retrieves a list of delegates, a list of themes, and it also selects a delegate
 * for a given theme by a citizen (voter).
 */
@Component
public class ChooseDelegateHandler {

  private final ThemeCatalog themeCatalog;
  private final DelegateThemeCatalog dtCatalog;
  private final CitizenCatalog citizenCatalog;

  /**
   * Constructs a new instance of the ChooseDelegateHandler class with the specified dependencies.
   *
   * @param themeCatalog the to use for retrieving themes.
   * @param dtCatalog the to use for retrieving delegate themes.
   * @param citizenCatalog the to use for retrieving citizens.
   */
  @Autowired
  public ChooseDelegateHandler(
      ThemeCatalog themeCatalog, DelegateThemeCatalog dtCatalog, CitizenCatalog citizenCatalog) {
    this.themeCatalog = themeCatalog;
    this.dtCatalog = dtCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  /**
   * Fetches delegates from database.
   *
   * @return a list of DelegateDTOs.
   */
  public List<DelegateDTO> getDelegates() {
    List<Delegate> delegates = citizenCatalog.getDelegates();
    return delegates.stream()
        .map(delegate -> new DelegateDTO(delegate.getName(), delegate.getCc()))
        .collect(Collectors.toList());
  }

  /**
   * Fetches themes from database.
   *
   * @return a list of ThemeDTOs.
   */
  public List<ThemeDTO> getThemes() {
    List<Theme> themes = themeCatalog.getThemes();
    return themes.stream()
        .map(theme -> new ThemeDTO(theme.getDesignation()))
        .collect(Collectors.toList());
  }

  /**
   * Selects a delegate for a given theme by a citizen (voter). If a delegate has already been
   * chosen for the theme, the voter's vote is added to the existing delegate theme, to avoid
   * multiple representation in the same field, i.e., multiple votes for one individual. If no
   * delegate has been chosen for the theme, a new delegate theme is created and added to {@code
   * DelegateThemeCatalog} which is a catalog for storing DelegateTheme objects.
   *
   * @param delegateCc Delegate identification.
   * @param themeDesignation Theme identification.
   * @param voterCc Citizen who is choosing DelegateTheme identification.
   * @throws CitizenNotFoundException if the delegate or voter are not found in the catalog.
   * @throws ThemeNotFoundException if the specified theme is not found in the catalog.
   */
  public void chooseDelegate(Integer delegateCc, String themeDesignation, Integer voterCc)
      throws ApplicationException {

    Delegate d = citizenCatalog.getDelegate(delegateCc);
    if (d == null) {
      throw new CitizenNotFoundException("Delegate with cc" + delegateCc + " not found.");
    }
    Theme t = themeCatalog.getTheme(themeDesignation);
    if (t == null) {
      throw new ThemeNotFoundException(
          "Theme with designation " + themeDesignation + " not found.");
    }
    Optional<Citizen> c = citizenCatalog.getCitizenByCc(voterCc);
    if (c.isEmpty()) {
      throw new CitizenNotFoundException("Citizen with cc" + voterCc + " not found.");
    }

    List<DelegateTheme> dt_list = dtCatalog.getAll();

    // Removes old delegates that have the same theme as the one chosen;
    removeOldDelegateTheme(t, c.get());

    boolean exists = false;
    for (int i = 0; i < dt_list.size() && !exists; i++) {
      DelegateTheme dt = dt_list.get(i);
      if (dt.checkDelegateTheme(d, t)) {
        exists = true;
        dt.addVoter(c.get());
        c.get().addDelegateTheme(dt);
      }
    }
    if (!exists) {
      DelegateTheme dt = new DelegateTheme(d, t);
      dt.addVoter(c.get());
      c.get().addDelegateTheme(dt);
      dtCatalog.addDT(dt);
    }
  }

  /**
   * Removes the delegate theme (i.e., the representation of a citizen by a delegate in a particular
   * theme) for the given citizen in the specified theme, if it exists. This method removes the
   * citizen from the voters list of the delegate theme, and removes the delegate theme from the
   * delegate themes list of the citizen.
   *
   * @param citizen The citizen for whom the delegate theme needs to be removed.
   * @param t The theme for which the delegate theme needs to be removed.
   */
  private void removeOldDelegateTheme(Theme t, Citizen citizen) {
    List<DelegateTheme> dt_list = citizen.getDelegateThemes();
    DelegateTheme delegateThemeToRemove = null;
    for (DelegateTheme dt : dt_list) {
      if (dt.checkTheme(t)) {
        delegateThemeToRemove = dt;
        dt.removeCitizenRep(citizen);
        break;
      }
    }
    if (delegateThemeToRemove != null) {
      citizen.removeDelegateTheme(delegateThemeToRemove);
    }
  }
}
