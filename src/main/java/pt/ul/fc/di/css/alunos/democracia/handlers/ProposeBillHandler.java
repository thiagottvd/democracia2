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

/**
 * Use case E.
 *
 * <p>Handler that proposes a bill and retrieves a list of all themes converting them to a list of
 * {@link ThemeDTO} objects.
 */
@Component
public class ProposeBillHandler {

  private final ThemeCatalog themeCatalog;
  private final BillCatalog billCatalog;
  private final CitizenCatalog citizenCatalog;

  /**
   * Constructor for the ProposeBillHandler class. It takes a ThemeCatalog, BillCatalog and
   * CitizenCatalog objects as parameter and sets them as an attributes.
   *
   * @param themeCatalog the catalog responsible for managing polls.
   * @param billCatalog the catalog responsible for managing bills.
   * @param citizenCatalog the catalog responsible for managing citizens.
   */
  @Autowired
  public ProposeBillHandler(
      ThemeCatalog themeCatalog, BillCatalog billCatalog, CitizenCatalog citizenCatalog) {
    this.themeCatalog = themeCatalog;
    this.billCatalog = billCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  /**
   * Returns a list of ThemeDTO objects representing all themes.
   *
   * @return a list of ThemeDTO objects representing all themes.
   */
  public List<ThemeDTO> getThemes() {
    List<Theme> themes = themeCatalog.getThemes();
    return themes.stream()
        .map(theme -> new ThemeDTO(theme.getDesignation()))
        .collect(Collectors.toList());
  }

  /**
   * Proposes a new bill with the given parameters.
   *
   * @param title the title of the bill.
   * @param description the description of the bill.
   * @param pdfData the pdf data of the bill.
   * @param expirationDate the expiration date of the bill.
   * @param themeDesignation the theme designation of the bill.
   * @param cc the citizen card number of the bill proposer.
   * @throws ApplicationException if the delegate or the theme are not found.
   */
  public void proposeBill(
      String title,
      String description,
      byte[] pdfData,
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
    billCatalog.saveBill(new Bill(title, description, pdfData, expirationDate, delegate, theme));
  }
}
