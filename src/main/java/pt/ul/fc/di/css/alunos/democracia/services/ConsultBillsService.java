package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;

@Service
public class ConsultBillsService {

  private final ConsultBillsHandler consultBillsHandler;

  @Autowired
  public ConsultBillsService(ConsultBillsHandler consultBIllsHandler) {
    this.consultBillsHandler = consultBIllsHandler;
  }

  /**
   * Returns a list of BillDTO objects representing all open bills.
   *
   * @return a list of BillDTO objects representing all open bills.
   */
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
  public BillDTO getBillDetails(Long billId) throws ApplicationException {
    return consultBillsHandler.getBillDetails(billId);
  }
}
