package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.handlers.ListActivePollsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ListActivePollsService;

/**
 * This class contains the test cases for the use case D. The class tests the system behaviour when
 * retrieving active polls and verifies the output type, system behaviour when there is no inactive
 *  * polls, and if the use main testing. It uses JUnit 5.
 */
@DataJpaTest
public class ListActivePollsUseCaseTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private PollRepository pollRepository;
  private PollCatalog pollCatalog;
  private ListActivePollsService listActivePollsService;

  /** Initializes the objects needed for each test case. */
  @BeforeEach
  public void init() {
    pollCatalog = new PollCatalog(pollRepository);
    ListActivePollsHandler listActivePollsHandler = new ListActivePollsHandler(pollCatalog);
    listActivePollsService = new ListActivePollsService(listActivePollsHandler);
  }

  /** Test case to test the output type (should be PollDTO). */
  @Test
  public void testOutputType() {
    // Creating and persisting test data
    Delegate d = new Delegate("d", 1);
    entityManager.persist(d);
    Theme t = new Theme("t", null);
    entityManager.persist(t);
    Bill b = new Bill("b", "b", null, LocalDate.now(), d, t);
    entityManager.persist(b);
    Poll p = new Poll(b);
    entityManager.persist(p);

    // Getting the active polls list (only one persisted)
    List<PollDTO> activePolls = listActivePollsService.getActivePolls();

    // Verifying that the list is not empty
    assertFalse(activePolls.isEmpty());

    // Verifying that the first element is a PollDTO
    assertEquals(PollDTO.class, activePolls.get(0).getClass());
  }

  /** Test case to test the system behaviour when there is no current active polls. */
  @Test
  public void testThereIsNoActivePolls() {
    // Given an empty database

    // When calling the method to retrieve active polls
    List<PollDTO> activePolls = listActivePollsService.getActivePolls();

    // Then the returned list should be empty
    assertEquals(0, activePolls.size());
  }

  /**
   * The test method creates and persists test data consisting of 50 Delegates, Themes, Bills, and
   * Polls, where every even numbered Poll is either approved or rejected. The test then calls the
   * "getActivePolls" method of the ListActivePollsService to retrieve all active Polls and verify
   * their status. The test passes if there are exactly 25 active Polls and the status of each
   * active Poll is equal to "ACTIVE".
   */
  @Test
  public void testGetActivePolls() {
    boolean approvedType = false;

    // Loop through 50 times to create and persist test data
    for (int i = 0; i < 50; i++) {
      Delegate d = new Delegate(String.valueOf(i), i);
      entityManager.persist(d);
      Theme t = new Theme(String.valueOf(i), null);
      entityManager.persist(t);
      Bill b = new Bill(String.valueOf(i + 1), String.valueOf(i), null, LocalDate.now(), d, t);
      entityManager.persist(b);
      Poll p = new Poll(b);
      entityManager.persist(p);

      // Alternate between setting the Poll status to APPROVED and REJECTED. It does it 25 times.
      if (i % 2 == 0) {
        if (approvedType) {
          p.setStatus(PollStatus.APPROVED);
        } else {
          p.setStatus(PollStatus.REJECTED);
        }
        approvedType = !approvedType;
      }
    }

    // Call the use case
    List<PollDTO> activePolls = listActivePollsService.getActivePolls();

    // Verify the results
    // Ensure that there are 25 active Polls returned
    assertEquals(25, activePolls.size());

    // Ensure that each PollDTO in the list has a status of ACTIVE.
    for (PollDTO pollDTO : activePolls) {
      assertEquals(PollStatus.ACTIVE, pollCatalog.getPollByTitle(pollDTO.getTitle()).getStatus());
    }
  }
}
