package pt.ul.fc.di.css.alunos.democracia.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ProposeBillHandler;

@Service
public class ProposeBillService {

  private final ProposeBillHandler proposeBillHandler;

  @Autowired
  public ProposeBillService(ProposeBillHandler proposeBillHandler) {
    this.proposeBillHandler = proposeBillHandler;
  }

  /**
   * Returns a list of ThemeDTO objects representing all themes.
   *
   * @return a list of ThemeDTO objects representing all themes.
   */
  public List<ThemeDTO> getThemes() {
    return proposeBillHandler.getThemes();
  }

  /**
   * Creates a new bill.
   *
   * @param title the title of a Bill
   * @param description description of Bill
   * @param pdf a file with Bill details
   * @param expirationDate expiration of Date
   * @param themeDesignation name of theme of Bill
   * @param cc delegate identification
   * @throws ApplicationException if there is an error retrieving the bill.
   */
  public void proposeBill(
      String title,
      String description,
      byte[] pdf,
      LocalDate expirationDate,
      String themeDesignation,
      Integer cc)
      throws ApplicationException {
    proposeBillHandler.proposeBill(title, description, pdf, expirationDate, themeDesignation, cc);
  }
}
