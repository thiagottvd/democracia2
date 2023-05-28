package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;

/**
 * Use case G.
 *
 * <p>Service class that that retrieves a list of all open bills and retrieves the details of a
 * specific bill calling the appropriate handler {@link ConsultBillsHandler}.
 */
@Service
public class ConsultBillsService {

  private final ConsultBillsHandler consultBillsHandler;

  /**
   * Constructor for the ConsultBillsService class. It takes a ConsultBillsHandler object as
   * parameter and sets it as an attribute.
   *
   * @param consultBIllsHandler the handler responsible for retrieving a list of all open bills and
   *     retrieving the details of a specific bill.
   */
  @Autowired
  public ConsultBillsService(ConsultBillsHandler consultBIllsHandler) {
    this.consultBillsHandler = consultBIllsHandler;
  }

  /**
   * Returns a list of BillDTO objects representing all open bills.
   *
   * @return a list of BillDTO objects representing all open bills.
   */
  @Transactional(readOnly = true)
  public List<BillDTO> getOpenBills() {
    return consultBillsHandler.getOpenBills();
  }

  /**
   * Returns a BillDTO object containing the details of the bill with the specified ID.
   *
   * @param billId the ID of the bill to retrieve.
   * @return a BillDTO object containing the details of the specified bill.
   * @throws ApplicationException if there is an error retrieving the bill.
   */
  @Transactional(readOnly = true)
  public BillDTO getBillDetails(Long billId) throws ApplicationException {
    return consultBillsHandler.getBillDetails(billId);
  }
}
