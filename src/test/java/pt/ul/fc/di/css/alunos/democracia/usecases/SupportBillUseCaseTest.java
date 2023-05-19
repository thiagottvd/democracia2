package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.*;
import pt.ul.fc.di.css.alunos.democracia.handlers.SupportBillHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.services.SupportBillService;

/** SupportBillUseCaseTest is a test class for the SupportBillService class. */
@DataJpaTest
public class SupportBillUseCaseTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private BillRepository billRepository;
  @Autowired private CitizenRepository citizenRepository;
  @Autowired private PollRepository pollRepository;

  private SupportBillHandler supportBillHandler;
  private SupportBillService supportBillService;

  /**
   * Sets up the necessary dependencies for testing the SupportBillService class. Initializes a
   * PollCatalog, BillCatalog, and CitizenCatalog with their corresponding repositories. Creates a
   * new SupportBillHandler instance with the initialized catalogs and sets the creation poll
   * trigger value to 5 for testing purposes. Finally, creates a new SupportBillService instance
   * with the created SupportBillHandler.
   */
  @BeforeEach
  public void setUp() {
    PollCatalog pollCatalog = new PollCatalog(pollRepository);
    BillCatalog billCatalog = new BillCatalog(billRepository);
    CitizenCatalog citizenCatalog = new CitizenCatalog(citizenRepository);

    supportBillHandler = new SupportBillHandler(billCatalog, citizenCatalog, pollCatalog);

    supportBillService = new SupportBillService(supportBillHandler);
  }

  /**
   * Test case to verify that attempting to support a non-existent bill should result in an
   * ApplicationException being thrown. Also verifies that the specific exception thrown is a
   * BillNotFoundException.
   */
  @Test
  @DisplayName("Supporting a non-existent bill should throw exception")
  public void testSupportNonExistentBill() {
    assertThrows(ApplicationException.class, () -> supportBillService.supportBill(1L, 1));
    assertThrows(BillNotFoundException.class, () -> supportBillService.supportBill(1L, 1));
  }

  /**
   * Tests that attempting to support a bill with a non-existent citizen should throw an exception.
   *
   * <p>This test creates a new bill and attempts to support it with a non-existent citizen ID. The
   * test expects that an ApplicationException will be thrown, and that the cause of the exception
   * will be a CitizenNotFoundException.
   */
  @Test
  public void testSupportBillWithNonExistentCitizen() {
    Bill testBill = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    billRepository.save(testBill);

    assertThrows(
        ApplicationException.class, () -> supportBillService.supportBill(testBill.getId(), 1));
    assertThrows(
        CitizenNotFoundException.class, () -> supportBillService.supportBill(testBill.getId(), 1));
  }

  /**
   * Tests the supportBill method of the SupportBillService class when supporting a bill with an
   * existing citizen.
   */
  @Test
  public void testSupportBillWithExistingCitizen() {
    Bill testBill = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    billRepository.save(testBill);

    Citizen testCitizen = new Citizen("Thiago", 123);
    citizenRepository.save(testCitizen);

    assertDoesNotThrow(() -> supportBillService.supportBill(testBill.getId(), testCitizen.getCc()));
  }

  /**
   * Tests the behavior of the SupportBillService when trying to support a bill that has already
   * been supported by the same citizen.
   *
   * @throws ApplicationException if there is an error in the application
   */
  @Test
  public void testSupportBillMoreThanOnce() throws ApplicationException {
    Bill b = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    Citizen c = new Citizen("Thiago", 123);
    billRepository.save(b);
    citizenRepository.save(c);

    // Ensure the bill has one supporter
    assertEquals(1, b.getNumSupporters());

    // Support the bill with a new citizen
    supportBillService.supportBill(b.getId(), c.getCc());

    // Ensure the bill has two supporters
    assertEquals(2, b.getNumSupporters());

    // Attempt to support the bill with the same citizen, and assert that it fails
    assertThrows(
        CitizenAlreadySupportsBillException.class,
        () -> supportBillService.supportBill(b.getId(), c.getCc()));

    // Ensure the bill still has two supporters
    assertEquals(2, b.getNumSupporters());
  }

  /**
   * This method tests the functionality of the SupportBillService by verifying the correct update
   * of the number of supporters for bills, when citizens decide to support them.
   */
  @Test
  public void testSupportBill() throws ApplicationException {
    // Create bills and citizens to use in the test
    Bill b1 = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    Bill b2 = new Bill("Bill2", "desc bill2", null, LocalDate.now(), null, null);
    Bill b3 = new Bill("Bill3", "desc bill3", null, LocalDate.now(), null, null);
    Citizen c1 = new Citizen("Thiago", 123);
    Citizen c2 = new Citizen("Ivo", 222);
    Citizen c3 = new Citizen("Izuna", 412);

    // Save bills and citizens in their respective repositories
    billRepository.saveAll(Arrays.asList(b1, b2, b3));
    citizenRepository.saveAll(Arrays.asList(c1, c2, c3));

    // Verify initial number of supporters for each bill
    assertEquals(b1.getNumSupporters(), 1);
    assertEquals(b2.getNumSupporters(), 1);
    assertEquals(b3.getNumSupporters(), 1);

    // Citizen c supports bill b1
    supportBillService.supportBill(b1.getId(), c1.getCc());
    assertEquals(b1.getNumSupporters(), 2);
    assertEquals(b2.getNumSupporters(), 1);
    assertEquals(b3.getNumSupporters(), 1);

    // Citizens c2 and c3 support bill b1
    supportBillService.supportBill(b1.getId(), c2.getCc());
    supportBillService.supportBill(b1.getId(), c3.getCc());

    // Verify the number of supporters for each bill after new supporters
    assertEquals(b1.getNumSupporters(), 4);
    assertEquals(b2.getNumSupporters(), 1);
    assertEquals(b3.getNumSupporters(), 1);

    // Citizens c2 and c3 support bill b2
    supportBillService.supportBill(b2.getId(), c2.getCc());
    supportBillService.supportBill(b2.getId(), c3.getCc());

    // Verify the number of supporters for each bill after new supporters
    assertEquals(b1.getNumSupporters(), 4);
    assertEquals(b2.getNumSupporters(), 3);
    assertEquals(b3.getNumSupporters(), 1);

    // Citizen c3 supports bill b3
    supportBillService.supportBill(b3.getId(), c3.getCc());

    // Verify the number of supporters for each bill after new supporter
    assertEquals(b1.getNumSupporters(), 4);
    assertEquals(b2.getNumSupporters(), 3);
    assertEquals(b3.getNumSupporters(), 2);
  }

  /**
   * This method tests the creation of a poll associated with a bill, after the bill reaches the
   * required number of supporters.
   */
  @Test
  public void testSupportBillPollCreation() throws ApplicationException {
    // Set the creation poll trigger value to 5 for testing purposes
    supportBillHandler.setCreationPollTriggerValue(5);

    // create a delegate and a theme for the bill
    Delegate d = new Delegate("a", 40);
    Theme t = new Theme("s", null);
    entityManager.persist(d);
    entityManager.persist(t);

    // create a new bill with the delegate and theme, and save it to the repository
    Bill b = new Bill("Bill1", "desc bill1", null, LocalDate.now(), d, t);
    billRepository.save(b);

    // assert that the bill does not have a poll associated with it
    assertNull(b.getPoll());

    // for 4 citizens, support the bill and assert that the number of supporters
    // has increased and that the bill status is OPEN
    for (int i = 1; i < 5; i++) {
      assertNull(b.getPoll());
      assertEquals(b.getNumSupporters(), i);
      assertEquals(BillStatus.OPEN, b.getStatus());
      Citizen c = new Citizen(String.valueOf(i), i);
      citizenRepository.save(c);
      supportBillService.supportBill(b.getId(), c.getCc());
    }

    // assert that the bill has 5 supporters (the necessary number to create a poll)
    // and a poll associated with it, and that its status is now CLOSED
    assertEquals(5, b.getNumSupporters());
    assertNotEquals(null, b.getPoll());
    assertEquals(BillStatus.CLOSED, b.getStatus());

    // assert that the poll is associated with the correct bill and has the correct
    // closing date, as well as the expected number of positive and negative votes
    assertEquals(b, b.getPoll().getAssociatedBill());
    assertEquals(b.getExpirationDate(), b.getPoll().getClosingDate());
    assertEquals(1, b.getPoll().getNumPositiveVotes());
    assertEquals(0, b.getPoll().getNumNegativeVotes());

    // create a new citizen and attempt to support the bill again, which should throw
    // a VoteInClosedBillException
    Citizen c2 = new Citizen("c2", 999);
    citizenRepository.save(c2);
    assertThrows(
        VoteInClosedBillException.class,
        () -> supportBillService.supportBill(b.getId(), c2.getCc()));
  }
}
