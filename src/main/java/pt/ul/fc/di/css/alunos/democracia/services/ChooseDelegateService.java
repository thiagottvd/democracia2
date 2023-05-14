package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.DelegateDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ChooseDelegateHandler;

/**
 * Use case F.
 *
 * <p>Service class that retrieves a list of delegates, a list of themes, and it also selects a
 * delegate for a given theme by a citizen (voter). It calls the appropriate {@link
 * ChooseDelegateHandler} to handle those operations.
 */
@Service
public class ChooseDelegateService {

  private final ChooseDelegateHandler chooseDelegateHandler;

  /**
   * Constructor for the ChooseDelegateService class. It takes a ChooseDelegateHandler object as
   * parameter and sets it as an attribute.
   *
   * @param chooseDelegateHandler the handler responsible for retrieving a list of delegates and a
   *     list of themes, and it also selects a delegate for a given theme by a citizen (voter).
   */
  @Autowired
  public ChooseDelegateService(ChooseDelegateHandler chooseDelegateHandler) {
    this.chooseDelegateHandler = chooseDelegateHandler;
  }

  /**
   * Fetches delegates from database.
   *
   * @return a list of DelegateDTOs.
   */
  public List<DelegateDTO> getDelegates() {
    return chooseDelegateHandler.getDelegates();
  }

  /**
   * Fetches themes from database.
   *
   * @return a list of ThemeDTOs.
   */
  public List<ThemeDTO> getThemes() {
    return chooseDelegateHandler.getThemes();
  }

  /**
   * Selects a delegate for a given theme by a citizen (voter). If a delegate has already been
   * chosen for the theme, the voter's vote is added to the existing delegate theme, to avoid
   * multiple representation in the same field, i.e., multiple votes for one individual. If no
   * delegate has been chosen for the theme, a new delegate theme is created and added to {@code
   * DelegateThemeCatalog} which is a catalog for storing DelegateTheme objects.
   *
   * @param delegateCitizenCardNumber Delegate identification.
   * @param themeDesignation Theme identification.
   * @param voterCitizenCardNumber Citizen who is choosing DelegateTheme identification.
   * @throws ApplicationException if an unexpected error occurs while selecting the delegate.
   */
  public void chooseDelegate(
      Integer delegateCitizenCardNumber, String themeDesignation, Integer voterCitizenCardNumber)
      throws ApplicationException {
    chooseDelegateHandler.chooseDelegate(
        delegateCitizenCardNumber, themeDesignation, voterCitizenCardNumber);
  }
}
