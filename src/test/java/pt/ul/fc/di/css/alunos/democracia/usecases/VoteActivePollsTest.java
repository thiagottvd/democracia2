package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.*;
import pt.ul.fc.di.css.alunos.democracia.exceptions.*;
import pt.ul.fc.di.css.alunos.democracia.handlers.VoteActivePollsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.services.VoteActivePollsService;

/** VoteActivePollsTest is a test class for the VoteActivePollsService class. */
@DataJpaTest
public class VoteActivePollsTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private CitizenRepository citizenRepository;
  @Autowired private PollRepository pollRepository;
  private PollCatalog pollCatalog;
  private VoteActivePollsService voteActivePollsService;

  /**
   * Initializes the VoteActivePollsService instance by creating and configuring the necessary
   * objects, such as CitizenCatalog, PollCatalog, VoteActivePollsHandler, and
   * VoteActivePollsService. This method is annotated with @BeforeEach, indicating that it will be
   * executed before each test method is invoked. By doing so, it ensures that the
   * VoteActivePollsService instance is properly initialized before each test.
   */
  @BeforeEach
  public void init() {
    CitizenCatalog citizenCatalog = new CitizenCatalog(citizenRepository);
    pollCatalog = new PollCatalog(pollRepository);

    VoteActivePollsHandler voteActivePollsHandler =
        new VoteActivePollsHandler(pollCatalog, citizenCatalog);
    voteActivePollsService = new VoteActivePollsService(voteActivePollsHandler);
  }

  /** Test case to test the output type (should be PollDTO). */
  @Test
  public void testOutputType() throws InvalidDateException {
    // Creating and persisting test data
    byte[] bytes = {0x1};
    Delegate d = new Delegate("d", 1);
    entityManager.persist(d);
    Theme t = new Theme("t", null);
    entityManager.persist(t);
    Bill b = new Bill("bill", "desc", bytes, LocalDate.now(), d, t);
    entityManager.persist(b);
    Poll p = new Poll(b);
    entityManager.persist(p);

    // Getting the active polls list (only one persisted)
    List<PollDTO> activePolls = voteActivePollsService.getActivePolls();

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
    List<PollDTO> activeBills = voteActivePollsService.getActivePolls();

    // Then the returned list should be empty
    assertEquals(0, activeBills.size());
  }

  /**
   * The test method creates and persists test data consisting of 50 Delegates, Themes, Bills, and
   * Polls, where every even numbered Poll is either approved or rejected. The test then calls the
   * "getActivePolls" method of the ListActivePollsService to retrieve all active Polls and verify
   * their status. The test passes if there are exactly 25 active Polls and the status of each
   * active Poll is equal to "ACTIVE".
   */
  @Test
  public void testGetActivePolls() throws InvalidDateException {
    boolean approvedType = false;
    byte[] bytes = {0x1};

    // Loop through 50 times to create and persist test data
    for (int i = 100; i < 150; i++) {
      Delegate d = new Delegate(String.valueOf(i), i);
      entityManager.persist(d);
      Theme t = new Theme(String.valueOf(i), null);
      entityManager.persist(t);
      Bill b = new Bill(String.valueOf(i + 1), String.valueOf(i), bytes, LocalDate.now(), d, t);
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
    List<PollDTO> activePolls = voteActivePollsService.getActivePolls();

    // Verify the results
    // Ensure that there are 25 active Polls returned
    assertEquals(25, activePolls.size());

    // Ensure that each PollDTO in the list has a status of ACTIVE.
    for (PollDTO pollDTO : activePolls) {
      assertEquals(
          PollStatus.ACTIVE,
          pollCatalog.getPollByTitle(pollDTO.getTitle()).orElseThrow().getStatus());
    }
  }

  /** Tests vote method validation. */
  @Test
  public void testVoteValidation() throws InvalidDateException {
    byte[] bytes = {0x1};
    Theme t = new Theme("t", null);
    entityManager.persist(t);
    Bill b = new Bill("Bill1", "desc bill1", bytes, LocalDate.now(), new Delegate("d", 2), t);
    entityManager.persist(b);
    Poll p = new Poll(b);

    Citizen voter = new Citizen("c", 1);
    entityManager.persist(voter);

    assertThrows(
        PollNotFoundException.class,
        () -> voteActivePollsService.vote(null, voter.getCitizenCardNumber(), VoteType.POSITIVE));

    entityManager.persist(p);
    assertThrows(
        InvalidVoteTypeException.class,
        () ->
            voteActivePollsService.vote(
                p.getAssociatedBill().getTitle(), voter.getCitizenCardNumber(), null));

    entityManager.remove(voter);
    assertThrows(
        CitizenNotFoundException.class,
        () ->
            voteActivePollsService.vote(
                p.getAssociatedBill().getTitle(), voter.getCitizenCardNumber(), VoteType.NEGATIVE));
  }

  /** Tests checkDelegateVote method validation. */
  @Test
  public void testCheckDelegateVoteValidation() throws InvalidDateException {
    byte[] bytes = {0x1};
    Theme t = new Theme("t", null);
    entityManager.persist(t);
    Bill b = new Bill("Bill1", "desc bill1", bytes, LocalDate.now(), new Delegate("d", 2), t);
    entityManager.persist(b);
    Poll p = new Poll(b);

    Citizen voter = new Citizen("c", 1);
    entityManager.persist(voter);

    assertThrows(
        PollNotFoundException.class,
        () -> voteActivePollsService.checkDelegateVote(null, voter.getCitizenCardNumber()));

    entityManager.persist(p);
    entityManager.remove(voter);
    assertThrows(
        CitizenNotFoundException.class,
        () ->
            voteActivePollsService.checkDelegateVote(
                p.getAssociatedBill().getTitle(), voter.getCitizenCardNumber()));
  }

  /**
   * Unit test for ensuring that a CitizenAlreadyVotedException is thrown when a citizen or delegate
   * tries to vote more than once on the same poll.
   */
  @Test
  public void testVoteMoreThanOnce() throws ApplicationException {
    byte[] bytes = {0x1};
    Delegate d = new Delegate("d", 99);
    entityManager.persist(d);
    Theme t = new Theme("t", null);
    entityManager.persist(t);
    Bill b = new Bill("Bill1", "desc bill1", bytes, LocalDate.now(), d, t);
    entityManager.persist(b);
    Poll p = new Poll(b);
    entityManager.persist(p);
    Citizen voter = new Citizen("c", 1);
    entityManager.persist(voter);

    // citizen first vote
    voteActivePollsService.vote(
        p.getAssociatedBill().getTitle(), voter.getCitizenCardNumber(), VoteType.NEGATIVE);

    // assert that a CitizenAlreadyVotedException is thrown when the citizen tries to vote again
    assertThrows(
        CitizenAlreadyVotedException.class,
        () ->
            voteActivePollsService.vote(
                p.getAssociatedBill().getTitle(), voter.getCitizenCardNumber(), VoteType.POSITIVE));

    /* assert that a CitizenAlreadyVotedException is thrown when the delegate tries to
    vote again (he has a POSITIVE vote by default) */
    assertThrows(
        CitizenAlreadyVotedException.class,
        () ->
            voteActivePollsService.vote(
                p.getAssociatedBill().getTitle(), d.getCitizenCardNumber(), VoteType.NEGATIVE));
  }

  /**
   * Tests that a citizen and a delegate can vote on a poll, and that the poll correctly counts the
   * votes.
   */
  @Test
  public void testVote() throws ApplicationException {
    // Set up needed data
    byte[] bytes = {0x1};
    Delegate d = new Delegate("d", 99);
    entityManager.persist(d);
    Theme t = new Theme("t", null);
    entityManager.persist(t);
    Bill b = new Bill("Bill1", "desc bill1", bytes, LocalDate.now(), d, t);
    entityManager.persist(b);
    Poll p = new Poll(b);
    entityManager.persist(p);

    /* Test that the poll starts with no negative votes, one positive vote from the delegate, and
    the delegate is recorded as a public voter */
    assertEquals(0, p.getNumNegativeVotes());
    assertEquals(1, p.getNumPositiveVotes());
    assertTrue(p.getPublicVoters().containsKey(d));

    List<Citizen> voters = new ArrayList<>();

    // Cast votes from six citizens, alternating between positive and negative votes
    for (int i = 0; i < 6; i++) {
      Citizen voter = new Citizen(String.valueOf(i), i);
      voters.add(voter);
      VoteType voteType = VoteType.POSITIVE;
      entityManager.persist(voter);

      if (i % 2 == 0) {
        voteType = VoteType.NEGATIVE;
      }

      // call method under test
      voteActivePollsService.vote(
          p.getAssociatedBill().getTitle(), voter.getCitizenCardNumber(), voteType);
    }

    // Test that the poll now has three negative votes and four positive votes (as expected)
    assertEquals(3, p.getNumNegativeVotes());
    assertEquals(4, p.getNumPositiveVotes());

    assertTrue(p.getPublicVoters().containsKey(d));

    // Test that all six voters are recorded as private voters
    for (int i = 0; i < 6; i++) {
      assertEquals(p.getPrivateVoters().get(i), voters.get(i));
    }

    Delegate d2 = new Delegate("d2", 100);
    entityManager.persist(d2);

    // check if delegate can also vote
    voteActivePollsService.vote(
        p.getAssociatedBill().getTitle(), d2.getCitizenCardNumber(), VoteType.NEGATIVE);
    assertTrue(p.getPublicVoters().containsKey(d2));
    assertEquals(4, p.getNumNegativeVotes());
  }

  /**
   * Test case for finding the delegate for a specific theme and checking the vote of a delegate.
   */
  @Test
  public void testFindDelegateForTheme() throws ApplicationException {
    // Create and persist needed data
    Delegate proposer = new Delegate("d", 13);
    entityManager.persist(proposer);

    Delegate delegate = new Delegate("d", 99);
    entityManager.persist(delegate);

    Theme theme = new Theme("t", null);
    entityManager.persist(theme);

    DelegateTheme dt = new DelegateTheme(delegate, theme);
    entityManager.persist(dt);

    Citizen voter = new Citizen("c", 97);
    entityManager.persist(voter);

    Citizen voterWithNoDelegates = new Citizen("c", 33);
    entityManager.persist(voterWithNoDelegates);

    byte[] bytes = {0x1};
    Bill b = new Bill("Bill1", "desc bill1", bytes, LocalDate.now(), proposer, theme);
    entityManager.persist(b);

    Poll p = new Poll(b);
    entityManager.persist(p);

    // Set delegate theme for voter and make delegate vote on poll
    voter.addDelegateTheme(dt);
    voteActivePollsService.vote(
        p.getAssociatedBill().getTitle(), delegate.getCitizenCardNumber(), VoteType.NEGATIVE);

    // Check that delegate's vote was properly registered
    VoteType actualVoteType =
        voteActivePollsService.checkDelegateVote(
            p.getAssociatedBill().getTitle(), voter.getCitizenCardNumber());
    assertEquals(VoteType.NEGATIVE, actualVoteType);

    // Check that a citizen with no delegates returns null when checking vote
    assertNull(
        voteActivePollsService.checkDelegateVote(
            p.getAssociatedBill().getTitle(), voterWithNoDelegates.getCitizenCardNumber()));
  }
}
