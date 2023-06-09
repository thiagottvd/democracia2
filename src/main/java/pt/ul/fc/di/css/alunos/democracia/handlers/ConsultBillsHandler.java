package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.BillNotFoundException;

/**
 * Handles use case G.
 *
 * <p>Handler that retrieves a list of all open bills and retrieves the details of a specific bill.
 */
@Component
public class ConsultBillsHandler {

  private final BillCatalog billCatalog;

  /**
   * Constructs a ConsultBillsHandler instance with a BillCatalog.
   *
   * @param billCatalog the catalog of bills.
   */
  @Autowired
  public ConsultBillsHandler(BillCatalog billCatalog) {
    this.billCatalog = billCatalog;
  }

  /**
   * Returns a list of BillDTO objects representing all open bills.
   *
   * @return a list of BillDTO objects representing all open bills.
   */
  public List<BillDTO> getOpenBills() {
    List<Bill> openBills = billCatalog.getOpenBills();
    return openBills.stream()
        .map(bill -> new BillDTO(bill.getId(), bill.getTitle()))
        .collect(Collectors.toList());
  }

  /**
   * Returns a BillDTO object containing the details of the bill with the specified ID.
   *
   * @param billId the ID of the bill to retrieve.
   * @return a BillDTO object containing the details of the specified bill.
   * @throws ApplicationException if there is an error retrieving the bill.
   */
  public BillDTO getBillDetails(Long billId) throws ApplicationException {
    Optional<Bill> bill = billCatalog.getBill(billId);
    if (bill.isEmpty()) {
      throw new BillNotFoundException("The bill with id " + billId + " was not found.");
    }
    return new BillDTO(bill.get());
  }
}
