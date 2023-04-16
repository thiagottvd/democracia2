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

  // Verificar se isto esta certo
  public void addBill(Bill bill){
    billRepository.save(bill);
  }

  /**
   * Returns a bill with the specified ID.
   *
   * @param id the ID of the bill to return.
   * @return an Optional containing the bill if found, otherwise an empty Optional.
   */
  public Optional<Bill> getBill(Long id) {
    return billRepository.findById(id);
  }

  /**
   * Returns a list of all open bills.
   *
   * @return a list of all open bills.
   */
  public List<Bill> getOpenBills() {
    return billRepository.findAllOpenBills(BillStatus.OPEN);
  }

  /** Closes all bills that have expired. */
  public void closeExpiredBills() {
    billRepository.closeExpiredBills(BillStatus.CLOSED);
  }
}
