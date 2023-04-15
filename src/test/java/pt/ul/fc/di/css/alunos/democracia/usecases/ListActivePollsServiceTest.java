package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.handlers.ListActivePollsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;

@DataJpaTest
public class ListActivePollsServiceTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private PollRepository pollRepository;

  private ListActivePollsHandler listActivePollsHandler;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    listActivePollsHandler = new ListActivePollsHandler(pollRepository);
  }

  @Test
  public void testGetActivePolls() {
    // Create the bills and polls
    Bill bill1 = new Bill("Bill 1", LocalDate.now());
    Poll poll1 = new Poll(bill1);
    entityManager.persist(bill1);
    entityManager.persist(poll1);

    Bill bill2 = new Bill("Bill 2", LocalDate.now().minusDays(5));
    Poll poll2 = new Poll(bill2);
    poll2.setStatus(PollStatus.APPROVED);
    entityManager.persist(bill2);
    entityManager.persist(poll2);

    Bill bill3 = new Bill("Bill 3", LocalDate.now().minusDays(1));
    Poll poll3 = new Poll(bill3);
    entityManager.persist(bill3);
    entityManager.persist(poll3);

    Bill bill4 = new Bill("Bill 4", LocalDate.now().minusDays(1));
    Poll poll4 = new Poll(bill4);
    poll4.setStatus(PollStatus.REJECTED);
    entityManager.persist(bill4);
    entityManager.persist(poll4);

    // Call the use case
    List<BillDTO> activeBills = listActivePollsHandler.getActivePolls();

    // Verify the results
    assertEquals(2, activeBills.size());
    assertEquals(bill1.getTitle(), "Bill 1");
    assertEquals(bill3.getTitle(), "Bill 3");
  }
}
