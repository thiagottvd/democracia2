package pt.ul.fc.di.css.alunos.democracia.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.SupportBillHandler;

/**
 * Use case H.
 *
 * <p>Service class for supporting a bill by a citizen. It delegates the operation to the {@link
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
   * @param citizenCardNumber the citizen card number of the citizen who supports the bill.
   * @throws ApplicationException if an error occurs during the support operation.
   */
  @Transactional
  public void supportBill(Long billId, Integer citizenCardNumber) throws ApplicationException {
    supportBillHandler.supportBill(billId, citizenCardNumber);
  }
}
