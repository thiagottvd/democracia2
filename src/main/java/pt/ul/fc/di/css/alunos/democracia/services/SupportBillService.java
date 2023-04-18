package pt.ul.fc.di.css.alunos.democracia.services;

import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.SupportBillHandler;

/**
 * Service class for supporting a bill by a citizen. It delegates the operation to the {@link
 * SupportBillHandler}.
 */
@Service
public class SupportBillService {

  private final SupportBillHandler supportBillHandler;

  /**
   * Constructs a new instance of the {@code SupportBillService} with the specified {@link
   * SupportBillHandler}.
   *
   * @param supportBillHandler the handler to delegate the support operation to.
   */
  public SupportBillService(SupportBillHandler supportBillHandler) {
    this.supportBillHandler = supportBillHandler;
  }

  /**
   * Supports a bill with the given ID by a citizen with the given citizen card number.
   *
   * @param billId the ID of the bill to be supported.
   * @param cc the citizen card number of the citizen who supports the bill.
   * @throws ApplicationException if an error occurs during the support operation.
   */
  public void supportBill(Long billId, Integer cc) throws ApplicationException {
    supportBillHandler.supportBill(billId, cc);
  }
}
