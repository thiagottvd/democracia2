package pt.ul.fc.di.css.alunos.democracia.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;

@Component
public class SupportBillHandler {

  private final BillCatalog billCatalog;
  private final CitizenCatalog citizenCatalog;

  @Autowired
  public SupportBillHandler(BillCatalog billCatalog, CitizenCatalog citizenCatalog) {
    this.billCatalog = billCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  public void supportBill(Long billId, int nif) {
    // TODO
    checkNumOfSupports();
  }

  private void checkNumOfSupports() {
    // TODO
    // createPoll();
  }

  private void createPoll() {
    // TODO
  }
}
