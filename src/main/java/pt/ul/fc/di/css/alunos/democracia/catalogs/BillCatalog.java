package pt.ul.fc.di.css.alunos.democracia.catalogs;

import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;

@Component
public class BillCatalog {

  private final BillRepository billRepository;

  public BillCatalog(BillRepository billRepository) {
    this.billRepository = billRepository;
  }

  public void closeExpiredBills() {
    billRepository.closeExpiredBills(BillStatus.CLOSED);
  }
}
