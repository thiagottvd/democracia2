package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.BillNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;

/**
 * Handles use case H.
 *
 * <p>Handles the support of bills by citizens and the creation of polls for bills that have reached
 * a certain number of supporters.
 */
@Component
public class SupportBillHandler {
  /*
   This variable should ideally be final, but it has been made mutable to facilitate
   changes to its value during JUnit testing. The method {@code setCreationPollTriggerValue()}
   allows for dynamic updates to this variable, which is useful for testing scenarios
   where different values need to be tested without modifying the code directly.
  */
  private static int CREATION_POLL_TRIGGER_VALUE = 10000;
  private final BillCatalog billCatalog;
  private final CitizenCatalog citizenCatalog;
  private final PollCatalog pollCatalog;

  /**
   * Creates a new SupportBillHandler instance with the given catalogs.
   *
   * @param billCatalog the catalog of bills.
   * @param citizenCatalog the catalog of citizens.
   * @param pollCatalog the catalog of polls.
   */
  @Autowired
  public SupportBillHandler(
      BillCatalog billCatalog, CitizenCatalog citizenCatalog, PollCatalog pollCatalog) {
    this.billCatalog = billCatalog;
    this.citizenCatalog = citizenCatalog;
    this.pollCatalog = pollCatalog;
  }

  /**
   * Adds the support of a citizen identified by its citizen card to a given bill.
   *
   * @param billId the ID of the bill to support.
   * @param cc the citizen card of the citizen who wants to support the bill.
   * @throws ApplicationException if the bill or the citizen are not found.
   */
  public void supportBill(Long billId, Integer cc) throws ApplicationException {
    Optional<Bill> bill = billCatalog.getBill(billId);
    if (bill.isEmpty()) {
      throw new BillNotFoundException("The bill \"" + billId + "\" was not found.");
    }

    Optional<Citizen> citizen = citizenCatalog.getCitizenByCc(cc);
    if (citizen.isEmpty()) {
      throw new CitizenNotFoundException("The citizen with cc \"" + cc + "\" was not found.");
    }

    Bill b = bill.get();
    b.addSupporterVote(citizen.get());
    billCatalog.saveBill(b);

    checkNumOfSupports(bill.get());
  }

  /**
   * Checks if a bill has reached the required number of supporters to create a poll, and creates
   * the poll if so.
   *
   * @param bill the bill to check.
   */
  private void checkNumOfSupports(Bill bill) {
    if (bill.getNumSupporters() >= CREATION_POLL_TRIGGER_VALUE) {
      createPoll(bill);
    }
  }

  /**
   * Creates a new poll for the given bill and sets the bill status to {@code CLOSED}.
   *
   * @param bill the bill for which to create the poll.
   */
  private void createPoll(Bill bill) {
    /*
    TODO: com uma data de fecho igual à data de expiração do projecto de
    lei, com um limite mínimo de 15 dias e um limite máximo de 2 meses
    */
    Poll poll = new Poll(bill);
    bill.setPoll(poll);
    bill.setStatus(BillStatus.CLOSED);
    pollCatalog.savePoll(poll);
  }

  /**
   * Sets the value of the {@code CREATION_POLL_TRIGGER_VALUE} constant.
   *
   * <p>This method is intended to be used only for JUnit testing purposes and should not be used in
   * production code.
   *
   * @param newValue the new value of the {@code CREATION_POLL_TRIGGER_VALUE} constant to be set.
   */
  public void setCreationPollTriggerValue(int newValue) {
    CREATION_POLL_TRIGGER_VALUE = newValue;
  }
}
