package pt.ul.fc.di.css.alunos.democracia.usecases;

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
import pt.ul.fc.di.css.alunos.democracia.handlers.ListPastPollsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ListPastPollsService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test cases for the use case C. The class tests the system behaviour when
 * retrieving inactive polls and verifies the output type, system behaviour when there is no inactive
 * polls, and if the use main testing. It uses JUnit 5.
 */
@DataJpaTest
public class ListPastPollsServiceUseCaseTest {

    @Autowired private TestEntityManager entityManager;
    @Autowired
    private PollRepository pollRepository;
    PollCatalog pollCatalog;
    private ListPastPollsService listPastPollsService;

    /** Initializes the objects needed for each test case. */
    @BeforeEach
    public void init() {
        pollCatalog = new PollCatalog(pollRepository);
        ListPastPollsHandler listPastPollsHandler = new ListPastPollsHandler(pollCatalog);
        listPastPollsService = new ListPastPollsService(listPastPollsHandler);
    }

    /** Test case to test the output type (should be PollDTO). */
    @Test
    public void testOutputType() {
        // Creating and persisting test data
        byte[] bytes = {0x1};
        Delegate d = new Delegate("d", 1);
        entityManager.persist(d);
        Theme t = new Theme("t", null);
        entityManager.persist(t);
        Bill b = new Bill("b", "b", bytes, LocalDate.now(), d, t);
        entityManager.persist(b);
        Poll p = new Poll(b);
        entityManager.persist(p);

        // Setting its status to rejected (could also be approved) to become an inactive poll.
        p.setStatus(PollStatus.REJECTED);

        // Getting the inactive polls list (only one persisted)
        List<PollDTO> inactivePolls = listPastPollsService.getInactivePolls();

        // Verifying that the list is not empty
        assertFalse(inactivePolls.isEmpty());

        // Verifying that the first element is a PollDTO
        assertEquals(PollDTO.class, inactivePolls.get(0).getClass());
    }

    /** Test case to test the system behaviour when there is no inactive polls. */
    @Test
    public void testThereIsNoInactivePolls() {
        // Creating and persisting test data
        byte[] bytes = {0x1};
        Delegate d = new Delegate("d", 1);
        entityManager.persist(d);
        Theme t = new Theme("t", null);
        entityManager.persist(t);
        Bill b = new Bill("b", "b", bytes, LocalDate.now(), d, t);
        entityManager.persist(b);
        Poll p = new Poll(b);

        // Persisting an active poll
        entityManager.persist(p);

        // When calling the method to retrieve inactive polls
        List<PollDTO> inactivePolls = listPastPollsService.getInactivePolls();

        // Then the returned list should be empty
        assertEquals(0, inactivePolls.size());
    }

    /**
     * The test method creates and persists test data consisting of 50 Delegates, Themes, Bills, and
     * Polls, where every even numbered Poll is either approved or rejected. The test then calls the
     * "getInactivePolls" method of the ListPastPollsService to retrieve all inactive (approved or rejected)
     * Polls and verify their status. The test passes if there are exactly 25 inactive Polls and the status of each
     * inactive Poll is equal to "APPROVED" or "REJECTED".
     */
    @Test
    public void testGetInactivePolls() {
        boolean approvedType = false;
        byte[] bytes = {0x1};

        // Loop through 50 times to create and persist test data
        for (int i = 0; i < 50; i++) {
            Delegate d = new Delegate(String.valueOf(i), i);
            entityManager.persist(d);
            Theme t = new Theme(String.valueOf(i), null);
            entityManager.persist(t);
            Bill b = new Bill(String.valueOf(i + 1), String.valueOf(i), bytes, LocalDate.now(), d, t);
            entityManager.persist(b);
            Poll p = new Poll(b);

            // Alternate between setting the Poll status to APPROVED and REJECTED. It does it 25 times.
            if (i % 2 == 0) {
                if (approvedType) {
                    p.setStatus(PollStatus.APPROVED);
                } else {
                    p.setStatus(PollStatus.REJECTED);
                }
                approvedType = !approvedType;
            }
            entityManager.persist(p);
        }

        // Call the use case
        List<PollDTO> inactivePolls = listPastPollsService.getInactivePolls();

        // Verify the results
        // Ensure that there are 25 inactive Polls returned
        assertEquals(25, inactivePolls.size());

        // Ensure that each PollDTO in the list has a status of "APPROVED" or "REJECTED".
        for (PollDTO pollDTO : inactivePolls) {
            Poll poll = pollCatalog.getPollByTitle(pollDTO.getTitle());
            assertTrue(poll.getStatus() == PollStatus.APPROVED || poll.getStatus() == PollStatus.REJECTED);
        }
    }
}
