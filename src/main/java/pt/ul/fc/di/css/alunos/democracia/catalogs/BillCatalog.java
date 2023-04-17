package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;

/**
 * The BillCatalog class is responsible for managing bills by providing operations to retrieve, save
 * and close them. It uses a BillRepository to perform the database operations.
 */
@Component
public class BillCatalog {

  private final BillRepository billRepository;

  /**
   * Constructs a BillCatalog instance with the specified BillRepository instance.
   *
   * @param billRepository the BillRepository instance to use for performing database operations.
   */
  @Autowired
  public BillCatalog(BillRepository billRepository) {
    this.billRepository = billRepository;
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

  /**
   * Saves a bill to the database.
   *
   * @param bill the bill to save.
   */
  public void saveBill(Bill bill) {
    billRepository.save(bill);
  }
}
