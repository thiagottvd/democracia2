package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ThemeNotFoundException;

@Component
public class ProposeBillHandler {

  private final ThemeCatalog themeCatalog;
  private final BillCatalog billCatalog;
  private final CitizenCatalog citizenCatalog;

  @Autowired
  public ProposeBillHandler(
      ThemeCatalog themeCatalog, BillCatalog billCatalog, CitizenCatalog citizenCatalog) {
    this.themeCatalog = themeCatalog;
    this.billCatalog = billCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  /**
   * Returns a list of all available themes.
   *
   * @return A list of all available themes.
   */
  public List<ThemeDTO> getThemes() {
    List<Theme> themes = themeCatalog.getThemes();
    return themes.stream()
        .map(theme -> new ThemeDTO(theme.getDesignation()))
        .collect(Collectors.toList());
  }

  /**
   * Creates a new bill and adds it to the open bills list.
   *
   * @param title The new bill title.
   * @param description The new bill description.
   * @param pdf The new bill pdf file with the main content.
   * @param expirationDate The new bill expiration date.
   * @param cc The new bill delegate.
   * @param themeDesignation The new bill theme.
   */
  public void proposeBill(
      String title,
      String description,
      byte[] pdf,
      LocalDate expirationDate,
      String themeDesignation,
      Integer cc)
      throws ApplicationException {
    Delegate delegate = citizenCatalog.getDelegate(cc);
    if (delegate == null) {
      throw new CitizenNotFoundException("The delegate with cc " + cc + " was not found.");
    }
    Theme theme = themeCatalog.getTheme(themeDesignation);
    if (theme == null) {
      throw new ThemeNotFoundException(
          "The theme with title " + themeDesignation + " was not found.");
    }
    billCatalog.saveBill(new Bill(title, description, pdf, expirationDate, delegate, theme));
  }
}
