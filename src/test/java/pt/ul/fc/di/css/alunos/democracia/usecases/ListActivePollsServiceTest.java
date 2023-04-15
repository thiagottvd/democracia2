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
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.handlers.ListActivePollsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;

@DataJpaTest
public class ListActivePollsServiceTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private PollRepository pollRepository;
  private PollCatalog pollCatalog;
  private ListActivePollsHandler listActivePollsHandler;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    pollCatalog = new PollCatalog(pollRepository);
    listActivePollsHandler = new ListActivePollsHandler(pollCatalog);
  }

  @Test
  public void testGetActivePolls() {
    // Create the bills and polls
    Bill bill1 = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
    Poll poll1 = new Poll(bill1);
    entityManager.persist(bill1);
    entityManager.persist(poll1);

    Bill bill2 = new Bill("Bill2", "desc bill2", null, LocalDate.now().minusDays(1), null, null);
    Poll poll2 = new Poll(bill2);
    poll2.setStatus(PollStatus.APPROVED);
    entityManager.persist(bill2);
    entityManager.persist(poll2);

    Bill bill3 = new Bill("Bill3", "desc bill3", null, LocalDate.now().minusDays(2), null, null);
    Poll poll3 = new Poll(bill3);
    entityManager.persist(bill3);
    entityManager.persist(poll3);

    Bill bill4 = new Bill("Bill4", "desc bill4", null, LocalDate.now().minusDays(3), null, null);
    Poll poll4 = new Poll(bill4);
    poll4.setStatus(PollStatus.REJECTED);
    entityManager.persist(bill4);
    entityManager.persist(poll4);

    // Call the use case
    List<PollDTO> activeBills = listActivePollsHandler.getActivePolls();

    // Verify the results
    assertEquals(2, activeBills.size());
    assertEquals(bill1.getTitle(), "Bill1");
    assertEquals(bill3.getTitle(), "Bill3");
  }
}
