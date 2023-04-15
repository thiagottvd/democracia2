package pt.ul.fc.di.css.alunos.democracia.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;

@Component
public class CloseExpiredBillsHandler {

  private final BillCatalog billCatalog;

  @Autowired
  public CloseExpiredBillsHandler(BillCatalog billCatalog) {
    this.billCatalog = billCatalog;
  }

  public void closeExpiredBills() {
    billCatalog.closeExpiredBills();
  }
}
