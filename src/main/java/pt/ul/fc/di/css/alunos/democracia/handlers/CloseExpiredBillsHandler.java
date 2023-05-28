package pt.ul.fc.di.css.alunos.democracia.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;

/**
 * Handles use case F.
 *
 * <p>Handler that closes all expired bills.
 */
@Component
public class CloseExpiredBillsHandler {

  private final BillCatalog billCatalog;

  /**
   * Constructor for the CloseExpiredBillsHandler class. It takes a BillCatalog object as parameter
   * and sets it as an attribute.
   *
   * @param billCatalog the catalog responsible for managing bills.
   */
  @Autowired
  public CloseExpiredBillsHandler(BillCatalog billCatalog) {
    this.billCatalog = billCatalog;
  }

  /** Closes all bills that have expired. */
  @Scheduled(fixedRate = 60000)
  public void closeExpiredBills() {
    billCatalog.closeExpiredBills();
  }
}
