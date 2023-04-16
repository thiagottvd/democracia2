package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;

@Component
public class BillCatalog {

  private final BillRepository billRepository;

  @Autowired
  public BillCatalog(BillRepository billRepository) {
    this.billRepository = billRepository;
  }

  public Optional<Bill> getBill(Long id) {
    return billRepository.findById(id);
  }

  public List<Bill> getOpenBills() {
    return billRepository.findAllOpenBills(BillStatus.OPEN);
  }

  public void closeExpiredBills() {
    billRepository.closeExpiredBills(BillStatus.CLOSED);
  }
}
