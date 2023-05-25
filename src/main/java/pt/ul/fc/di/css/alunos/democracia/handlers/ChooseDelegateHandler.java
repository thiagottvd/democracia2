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
import pt.ul.fc.di.css.alunos.democracia.exceptions.DuplicateDelegateThemeException;
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
        .map(delegate -> new DelegateDTO(delegate.getName(), delegate.getCitizenCardNumber()))
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
   * chosen for the theme, the delegate associated with the theme is updated, to avoid
   * multiple representation in the same field, i.e., multiple votes for one individual. If no
   * delegate has been chosen for the theme, a new delegate theme is created and added to {@code
   * DelegateThemeCatalog} which is a catalog for storing DelegateTheme objects.
   *
   * @param delegateCitizenCardNumber Delegate identification.
   * @param themeDesignation Theme identification.
   * @param voterCitizenCardNumber Citizen who is choosing DelegateTheme identification.
   * @throws CitizenNotFoundException if the delegate or voter are not found in the catalog.
   * @throws ThemeNotFoundException if the specified theme is not found in the catalog.
   * @throws DuplicateDelegateThemeException if the citizen already have a delegate associated
   *                                         with the specified theme.
   */
  public void chooseDelegate(
      Integer delegateCitizenCardNumber, String themeDesignation, Integer voterCitizenCardNumber)
      throws ApplicationException {

    Optional<Delegate> delegate =
        citizenCatalog.getDelegateByCitizenCardNumber(delegateCitizenCardNumber);
    if (delegate.isEmpty()) {
      throw new CitizenNotFoundException(
          "Delegate with citizen card number " + delegateCitizenCardNumber + " was not found.");
    }
    Optional<Theme> theme = themeCatalog.getTheme(themeDesignation);
    if (theme.isEmpty()) {
      throw new ThemeNotFoundException(
          "Theme with designation " + themeDesignation + " not found.");
    }
    Optional<Citizen> voter = citizenCatalog.getCitizenByCitizenCardNumber(voterCitizenCardNumber);
    if (voter.isEmpty()) {
      throw new CitizenNotFoundException(
          "Citizen with citizen card number " + voterCitizenCardNumber + " was not found.");
    }

    if (updateDelegateThemeForVoter(delegate.get(), theme.get(), voter.get())) {
      throw new DuplicateDelegateThemeException(
              "The delegate associated with the theme " + themeDesignation + " was successfully updated.");
    }

    createDelegateTheme(delegate.get(), theme.get(), voter.get());
  }

  /**
   * Updates a DelegateTheme object in the voter's list if necessary. It is necessary when a
   * DelegateTheme object with the same Theme already exists in the voter's list.
   *
   * @param delegate The delegate to be associated with the theme.
   * @param theme    The theme to be associated with the delegate and voter.
   * @param voter    The voter for whom the delegate theme is being updated.
   * @return true if the DelegateTheme object was updated in the voter's list, false otherwise.
   */
  private boolean updateDelegateThemeForVoter(Delegate delegate, Theme theme, Citizen voter) {
    List<DelegateTheme> voterDelegateThemeList = voter.getDelegateThemes();
    for (DelegateTheme dt : voterDelegateThemeList) {
      if (dt.getTheme().equals(theme)) {
        disassociateDelegateThemeAndCitizen(dt, voter);
        createDelegateTheme(delegate, theme, voter);
        return true;
      }
    }
    return false;
  }

  /**
   * Disassociates the delegate theme and the voter. It removes the voter from the
   * delegate theme's voter list and removes the delegate theme from the voter's
   * delegate theme list. If there are no more voters associated with the
   * delegate theme, it is deleted from the catalog; otherwise, it is saved in the catalog.
   *
   * @param dt The delegate theme to be disassociated.
   * @param voter The voter to be disassociated from the delegate theme.
   */
  private void disassociateDelegateThemeAndCitizen(DelegateTheme dt, Citizen voter) {
    List<Citizen> voters = dt.getVoters();
    voters.remove(voter);
    voter.removeDelegateTheme(dt);
    if (voters.isEmpty()) {
      dtCatalog.deleteDelegateTheme(dt);
    } else {
      dtCatalog.saveDelegateTheme(dt);
    }
  }

  /**
   * Creates a new DelegateTheme object and associates it with the provided delegate, theme, and voter.
   *
   * @param delegate The delegate to be associated with the theme.
   * @param theme The theme to be associated with the delegate and voter.
   * @param voter The voter to be associated with the delegate and theme.
   */
  private void createDelegateTheme(Delegate delegate, Theme theme, Citizen voter) {
    DelegateTheme dt = new DelegateTheme(delegate, theme);
    dt.addVoter(voter);
    dtCatalog.saveDelegateTheme(dt);
  }

}
