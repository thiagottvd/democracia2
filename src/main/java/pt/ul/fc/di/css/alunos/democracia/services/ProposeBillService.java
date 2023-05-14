package pt.ul.fc.di.css.alunos.democracia.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ProposeBillHandler;

/**
 * Use case E.
 *
 * <p>This service class provides methods for proposing a bill and retrieving a list of all themes
 * calling the appropriate handler {@link ProposeBillHandler}.
 */
@Service
public class ProposeBillService {

  private final ProposeBillHandler proposeBillHandler;

  /**
   * Constructor for the ProposeBillService class. It takes a ProposeBillHandler object as parameter
   * and sets it as an attribute.
   *
   * @param proposeBillHandler proposeBillHandler the handler responsible for handling the logic
   *     related to proposing a bill and retrieving a list of all themes.
   */
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
   * Proposes a new bill with the given parameters and retrieves a BillDTO object representing the
   * proposed bill.
   *
   * @param title the title of the bill.
   * @param description the description of the bill.
   * @param pdfData the pdf data of the bill.
   * @param expirationDate the expiration date of the bill.
   * @param themeDesignation the theme designation of the bill.
   * @param citizenCardNumber the citizen card number of the bill proposer.
   * @throws ApplicationException if there is an exception during the bill proposal process.
   * @return a BillDTO object representing the proposed bill.
   */
  public BillDTO proposeBill(
      String title,
      String description,
      byte[] pdfData,
      LocalDate expirationDate,
      String themeDesignation,
      Integer citizenCardNumber)
      throws ApplicationException {
    return new BillDTO(
        proposeBillHandler.proposeBill(
            title, description, pdfData, expirationDate, themeDesignation, citizenCardNumber));
  }
}
