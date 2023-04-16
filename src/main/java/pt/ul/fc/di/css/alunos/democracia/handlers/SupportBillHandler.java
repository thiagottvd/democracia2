package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.BillNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;

@Component
public class SupportBillHandler {

  private final BillCatalog billCatalog;
  private final CitizenCatalog citizenCatalog;
  private final PollCatalog pollCatalog;

  @Autowired
  public SupportBillHandler(
      BillCatalog billCatalog, CitizenCatalog citizenCatalog, PollCatalog pollCatalog) {
    this.billCatalog = billCatalog;
    this.citizenCatalog = citizenCatalog;
    this.pollCatalog = pollCatalog;
  }

  public void supportBill(Long billId, int nif) throws ApplicationException {
    Optional<Bill> bill = billCatalog.getBill(billId);
    if (bill.isEmpty()) {
      throw new BillNotFoundException("The bill \"" + billId + "\" was not found.");
    }
    // Duvida: um delegado pode suportar projetos, mesmo que não criado por ele?
    Optional<Citizen> citizen = citizenCatalog.getCitizenByNif(nif);
    if (citizen.isEmpty()) {
      throw new CitizenNotFoundException("The citizen with nif \"" + nif + "\" was not found.");
    }

    bill.get().addSupporter(citizen.get());
    billCatalog.saveBill(bill.get());

    checkNumOfSupports(bill.get());
  }

  private void checkNumOfSupports(Bill bill) {
    if (bill.getNumSupporters() >= 10000) {
      createPoll(bill);
    }
  }

  private void createPoll(Bill bill) {
    // om uma data de fecho igual à data de expiração do projecto de
    // lei, com um limite mínimo de 15 dias e um limite máximo de 2 meses.?
    Poll poll = new Poll(bill);
    pollCatalog.savePoll(poll);
  }
}
