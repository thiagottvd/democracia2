package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.handlers.CloseExpiredBillsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;

/**
 * JUnit test class for the {@link CloseExpiredBillsHandler} use case. This class tests the
 * functionality of the {@link CloseExpiredBillsHandler} class, which is responsible for closing
 * bills that have expired. The test cases in this class include testing that expired bills are
 * correctly closed when there are no bills in the system and testing that expired bills are closed
 * while open bills remain open.
 */
@DataJpaTest
public class CloseExpiredBillsUseCaseTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private BillRepository billRepository;
  private BillCatalog billCatalog;
  private CloseExpiredBillsHandler closeExpiredBillsHandler;

  /** Setup method that initializes the necessary objects for the tests. */
  @BeforeEach
  public void init() {
    billCatalog = new BillCatalog(billRepository);
    closeExpiredBillsHandler = new CloseExpiredBillsHandler(billCatalog);
  }

  /**
   * Test case that checks if expired bills are correctly closed when there are no bills in the
   * system. It creates 5 bills with an expiration date set to tomorrow, ensures they are persisted,
   * calls the method under test to close expired bills, and checks that no bills were prematurely
   * closed.
   */
  @Test
  public void testCloseExpiredBillsWithNoBills() {
    // Bills will never be expired
    LocalDate localDate = LocalDate.now().plusDays(1);

    for (int i = 0; i < 5; i++) {
      Theme t = new Theme(String.valueOf(i), null);
      entityManager.persist(t);
      Delegate d = new Delegate(String.valueOf(i), i);
      entityManager.persist(d);

      Bill b =
          new Bill(
              String.valueOf(i), String.valueOf(i), String.valueOf(i).getBytes(), localDate, d, t);
      entityManager.persist(b);
    }

    // Ensure bills are persisted before running test
    entityManager.flush();
    entityManager.clear();

    // Call method under test
    closeExpiredBillsHandler.closeExpiredBills();

    // Check that bills were not closed prematurely
    List<Bill> bills = billCatalog.getOpenBills();
    assertEquals(5, bills.size());

    for (Bill b : bills) {
      assertEquals(BillStatus.OPEN, b.getStatus());
    }
  }

  /**
   * Test method to verify if the "closeExpiredBills" method closes bills that have expired and
   * leaves open bills that have not. The method sets up a scenario with 20 bills, some of which are
   * expired and others that are not, and then calls the method under test. After calling the
   * method, it checks if there are only open bills remaining in the catalog and if their status is
   * set to "OPEN".
   */
  @Test
  public void testCloseExpiredBills() {
    // Set up scenario with expired and unexpired bills
    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDate today = LocalDate.now();

    // Create 20 bills with different expiration (is expired (yesterday) or will expire (today))
    // dates and add them to the entity manager
    for (int i = 0; i < 20; i++) {
      Theme t = new Theme(String.valueOf(i), null);
      entityManager.persist(t);
      Delegate d = new Delegate(String.valueOf(i), i);
      entityManager.persist(d);

      // Create some bills that are expired and others that are not (10 OPEN and 10 CLOSED)
      LocalDate expiration = i % 2 == 0 ? yesterday : today;
      Bill b =
          new Bill(
              String.valueOf(i), String.valueOf(i), String.valueOf(i).getBytes(), expiration, d, t);
      entityManager.persist(b);
    }

    // Ensure bills are persisted before running test
    entityManager.flush();
    entityManager.clear();

    // Call method under test
    closeExpiredBillsHandler.closeExpiredBills();

    // Check if there is only opens bills remaining
    List<Bill> openBills = billCatalog.getOpenBills();
    assertEquals(10, openBills.size());

    // Check if the status of all open bills is set to "OPEN"
    for (Bill b : openBills) {
      assertEquals(BillStatus.OPEN, b.getStatus());
    }
  }
}
