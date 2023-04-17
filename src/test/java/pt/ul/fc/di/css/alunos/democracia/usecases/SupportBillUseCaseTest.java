package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.exceptions.*;
import pt.ul.fc.di.css.alunos.democracia.handlers.SupportBillHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.services.SupportBillService;

@DataJpaTest
public class SupportBillUseCaseTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private BillRepository billRepository;
  @Autowired private CitizenRepository citizenRepository;
  @Autowired private PollRepository pollRepository;
  private BillCatalog billCatalog;
  private CitizenCatalog citizenCatalog;
  private PollCatalog pollCatalog;
  private SupportBillHandler supportBillHandler;
  private SupportBillService supportBillService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    pollCatalog = new PollCatalog(pollRepository);
    billCatalog = new BillCatalog(billRepository);
    citizenCatalog = new CitizenCatalog(citizenRepository);
    supportBillHandler = new SupportBillHandler(billCatalog, citizenCatalog, pollCatalog);
    // Set the creation poll trigger value to 5 for testing purposes
    supportBillHandler.setCreationPollTriggerValue(5);
    supportBillService = new SupportBillService(supportBillHandler);
  }

  @Test
  public void testSupportBillValidation() {
    assertThrows(ApplicationException.class, () -> supportBillService.supportBill(1L, 1));
    assertThrows(BillNotFoundException.class, () -> supportBillService.supportBill(1L, 1));

    Bill b = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    billRepository.save(b);

    assertThrows(ApplicationException.class, () -> supportBillService.supportBill(1L, 1));
    assertThrows(CitizenNotFoundException.class, () -> supportBillService.supportBill(1L, 1));

    billRepository.delete(b);
    Citizen c = new Citizen("Thiago", 123);
    citizenRepository.save(c);

    assertThrows(ApplicationException.class, () -> supportBillService.supportBill(1L, 1));
    assertThrows(BillNotFoundException.class, () -> supportBillService.supportBill(1L, 1));
  }

  @Test
  public void testSupportBillMoreThanOneVote() throws ApplicationException {
    Bill b = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    Citizen c = new Citizen("Thiago", 123);

    billRepository.save(b);
    citizenRepository.save(c);

    assertEquals(b.getNumSupporters(), 0);

    supportBillService.supportBill(b.getId(), c.getCc());

    assertEquals(b.getNumSupporters(), 1);

    assertThrows(
        CitizenAlreadySupportsBillException.class,
        () -> supportBillService.supportBill(b.getId(), c.getCc()));

    assertEquals(b.getNumSupporters(), 1);
  }

  @Test
  public void testSupportBill() throws ApplicationException {
    Bill b = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    Bill b2 = new Bill("Bill2", "desc bill2", null, LocalDate.now(), null, null);
    Bill b3 = new Bill("Bill3", "desc bill3", null, LocalDate.now(), null, null);
    Citizen c = new Citizen("Thiago", 123);
    Citizen c2 = new Citizen("Ivo", 222);
    Citizen c3 = new Citizen("Izuna", 412);

    billRepository.save(b);
    billRepository.save(b2);
    billRepository.save(b3);
    citizenRepository.save(c);
    citizenRepository.save(c2);
    citizenRepository.save(c3);

    assertEquals(b.getNumSupporters(), 0);
    assertEquals(b2.getNumSupporters(), 0);
    assertEquals(b3.getNumSupporters(), 0);

    supportBillService.supportBill(b.getId(), c.getCc());
    assertEquals(b.getNumSupporters(), 1);
    assertEquals(b2.getNumSupporters(), 0);
    assertEquals(b3.getNumSupporters(), 0);

    supportBillService.supportBill(b.getId(), c2.getCc());
    supportBillService.supportBill(b.getId(), c3.getCc());

    assertEquals(b.getNumSupporters(), 3);
    assertEquals(b2.getNumSupporters(), 0);
    assertEquals(b3.getNumSupporters(), 0);

    supportBillService.supportBill(b2.getId(), c2.getCc());
    supportBillService.supportBill(b2.getId(), c3.getCc());

    assertEquals(b.getNumSupporters(), 3);
    assertEquals(b2.getNumSupporters(), 2);
    assertEquals(b3.getNumSupporters(), 0);

    supportBillService.supportBill(b3.getId(), c3.getCc());

    assertEquals(b.getNumSupporters(), 3);
    assertEquals(b2.getNumSupporters(), 2);
    assertEquals(b3.getNumSupporters(), 1);
  }

  @Test
  public void testSupportBillPollCreation() throws ApplicationException {
    Bill b = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    billRepository.save(b);

    assertEquals(b.getPoll(), null);

    for (int i = 0; i < 5; i++) {
      assertEquals(b.getPoll(), null);
      assertEquals(b.getNumSupporters(), i);
      assertEquals(BillStatus.OPEN, b.getStatus());
      Citizen c = new Citizen(String.valueOf(i), i);
      citizenRepository.save(c);
      supportBillService.supportBill(b.getId(), c.getCc());
    }

    // bill
    assertEquals(5, b.getNumSupporters());
    assertNotEquals(null, b.getPoll());
    assertEquals(BillStatus.CLOSED, b.getStatus());

    // poll
    assertEquals(b, b.getPoll().getAssociatedBill());
    assertEquals(b.getExpirationDate(), b.getPoll().closingDate());
    assertEquals(1, b.getPoll().getNumPositiveVotes());
    assertEquals(0, b.getPoll().getNumNegativeVotes());

    // citizen
    Citizen c2 = new Citizen("c2", 123);
    citizenRepository.save(c2);
    assertThrows(
        VoteInClosedBillException.class,
        () -> supportBillService.supportBill(b.getId(), c2.getCc()));
  }
}
