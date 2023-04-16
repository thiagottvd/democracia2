package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

@Component
public class ConsultBillsHandler {

  private final BillCatalog billCatalog;

  @Autowired
  public ConsultBillsHandler(BillCatalog billCatalog) {
    this.billCatalog = billCatalog;
  }

  /**
   * Method that returns a list with the titles of all open bills.
   *
   * @return a list with the titles of all open bills.
   */
  public List<BillDTO> getOpenBills() {
    List<Bill> openBills = billCatalog.getOpenBills();
    return openBills.stream()
        .map(bill -> new BillDTO(bill.getTitle()))
        .collect(Collectors.toList());
  }

  public BillDTO getBillDetails(BillDTO billDTO) {
    // TODO
    return null;
  }
}
