package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.handlers.CloseExpiredBillsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;

@DataJpaTest
public class CloseExpiredBillsTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private BillRepository billRepository;
  private BillCatalog billCatalog;
  private CloseExpiredBillsHandler closeExpiredBillsHandler;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    billCatalog = new BillCatalog(billRepository);
    closeExpiredBillsHandler = new CloseExpiredBillsHandler(billCatalog);
  }

  @Test
  public void testCloseExpiredBills() {
    // TODO - melhorar os testes
    // Create the bills with different expiration dates and statuses
    LocalDate today = LocalDate.now();
    Bill bill1 = new Bill("Bill1", "desc bill1", null, today, null, null);
    Bill bill2 = new Bill("Bill2", "desc bill2", null, today.minusDays(1), null, null);
    Bill bill3 = new Bill("Bill3", "desc bill3", null, today.minusDays(2), null, null);
    Bill bill4 = new Bill("Bill4", "desc bill4", null, today.minusDays(3), null, null);
    entityManager.persist(bill1);
    entityManager.persist(bill2);
    entityManager.persist(bill3);
    entityManager.persist(bill4);

    // Call the use case
    closeExpiredBillsHandler.closeExpiredBills();

    // Verify the results
    assertEquals(BillStatus.OPEN, billRepository.findById(bill1.getId()).get().getStatus());
    assertEquals(BillStatus.CLOSED, billRepository.findById(bill2.getId()).get().getStatus());
    assertEquals(BillStatus.CLOSED, billRepository.findById(bill3.getId()).get().getStatus());
    assertEquals(BillStatus.CLOSED, billRepository.findById(bill4.getId()).get().getStatus());
  }
}
