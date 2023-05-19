package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.entities.*;
import pt.ul.fc.di.css.alunos.democracia.handlers.ClosePollsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;

/** ClosePollsUseCaseTest is a test class for the ClosePollsHandler class. */
@DataJpaTest
public class ClosePollsUseCaseTest {
  @Autowired private TestEntityManager em;
  @Autowired private PollRepository pollRepository;
  @Autowired private CitizenRepository citizenRepository;
  private ClosePollsHandler closePollsHandler;

  @BeforeEach
  public void init() {
    CitizenCatalog citizenCatalog = new CitizenCatalog(citizenRepository);
    PollCatalog pollCatalog = new PollCatalog(pollRepository);
    closePollsHandler = new ClosePollsHandler(pollCatalog, citizenCatalog);
  }

  /**
   * Tests what happens when there is at least one expired poll and there are no delegates
   * representing non-voters.
   */
  @Test
  public void testClosePollsWithNoRepresentativesForCitizen() {
    // Creating and persisting needed data
    Delegate proposer = em.persist(new Delegate("proposer", 0));
    Theme saude = em.persist(new Theme("saude", null));
    Theme hospital = em.persist(new Theme("hospital", saude));
    Theme remedios = em.persist(new Theme("remedios", hospital));
    em.persist(new Citizen("c1", 55));
    Poll poll =
        em.persist(
            new Poll(
                new Bill(
                    "bTitle", "desc", null, LocalDate.now().minusDays(1), proposer, remedios)));

    // Calling the method being tested
    closePollsHandler.closePolls();

    // Checking that the poll has been closed and the vote counts are correct
    assertEquals(PollStatus.APPROVED, poll.getStatus());
    assertEquals(0, poll.getNumNegativeVotes());
    assertEquals(1, poll.getNumPositiveVotes());
  }

  /** Tests all behaviour of the use case main scenario. */
  @Test
  public void assignVotesAndClosePollsTest() {
    // Creating and persisting proposer and delegates
    Delegate proposer = em.persist(new Delegate("proposer", 0));
    Delegate deleg1 = em.persist(new Delegate("d1", 1));
    Delegate deleg2 = em.persist(new Delegate("d2", 2));
    Delegate deleg3 = em.persist(new Delegate("d3", 3));

    // Creating and persisting themes and subthemes
    Theme saude = em.persist(new Theme("saude", null));
    Theme profissionalSaude = em.persist(new Theme("profissional saude", saude));
    Theme medicos = em.persist(new Theme("medicos", profissionalSaude));
    Theme hospital = em.persist(new Theme("hospital", saude));
    Theme remedios = em.persist(new Theme("remedios", hospital));
    em.persist(new Theme("macas", hospital));
    Theme economia = em.persist(new Theme("economia", null));
    Theme impostos = em.persist(new Theme("impostos", economia));
    Theme orcamento = em.persist(new Theme("orcamento", economia));
    Theme gastos = em.persist(new Theme("gastos", orcamento));

    // Creating and persisting bills for each theme
    Bill saudeBill =
        em.persist(
            new Bill("saudeBill", "desc", null, LocalDate.now().minusDays(1), proposer, remedios));
    Bill economiaBill =
        em.persist(
            new Bill(
                "economiaBill", "desc", null, LocalDate.now().minusDays(1), proposer, impostos));

    // Creating and persisting polls with a bill as the subject
    Poll saudePoll = em.persist(new Poll(saudeBill));
    Poll economiaPoll = em.persist(new Poll(economiaBill));

    // Creating and persisting citizens who voted and adding their votes to the polls
    Citizen c1 = em.persist(new Citizen("c1", 55));
    Citizen c2 = em.persist(new Citizen("c2", 56));
    saudePoll.addPrivateVoter(c1, VoteType.POSITIVE);
    saudePoll.addPrivateVoter(c2, VoteType.NEGATIVE);
    Citizen c3 = em.persist(new Citizen("c3", 57));
    Citizen c4 = em.persist(new Citizen("c4", 58));
    economiaPoll.addPrivateVoter(c3, VoteType.POSITIVE);
    economiaPoll.addPrivateVoter(c4, VoteType.NEGATIVE);

    // Creating and persisting citizens who did not vote
    Citizen c5 = em.persist(new Citizen("c5", 59));
    Citizen c6 = em.persist(new Citizen("c6", 60));
    Citizen c7 = em.persist(new Citizen("c7", 61));
    Citizen c8 = em.persist(new Citizen("c8", 62));

    // Adding public voters and their votes to the polls
    saudePoll.addPublicVoter(deleg1, VoteType.NEGATIVE);
    saudePoll.addPublicVoter(deleg2, VoteType.POSITIVE);
    saudePoll.addPublicVoter(deleg3, VoteType.NEGATIVE);
    economiaPoll.addPublicVoter(deleg1, VoteType.POSITIVE);
    economiaPoll.addPublicVoter(deleg2, VoteType.POSITIVE);
    economiaPoll.addPublicVoter(deleg3, VoteType.NEGATIVE);

    // Assigning delegates to themes for citizens who did not vote
    // c5 - saudePoll vote should be NEGATIVE (deleg1), economiaPoll vote should be POSITIVE
    // (deleg1)
    DelegateTheme dt1 = em.persist(new DelegateTheme(deleg1, saude));
    DelegateTheme dt2 = em.persist(new DelegateTheme(deleg3, medicos));
    DelegateTheme dt3 = em.persist(new DelegateTheme(deleg1, hospital));
    DelegateTheme dt4 = em.persist(new DelegateTheme(deleg1, impostos));
    c5.addDelegateTheme(dt1);
    c5.addDelegateTheme(dt2);
    c5.addDelegateTheme(dt3);
    c5.addDelegateTheme(dt4);

    // c6 saudePoll vote should be NEGATIVE (deleg1), economiaPoll vote should be POSITIVE (deleg1)
    DelegateTheme dt5 = em.persist(new DelegateTheme(deleg1, saude));
    DelegateTheme dt6 = em.persist(new DelegateTheme(deleg2, medicos));
    DelegateTheme dt7 = em.persist(new DelegateTheme(deleg1, hospital));
    DelegateTheme dt8 = em.persist(new DelegateTheme(deleg2, orcamento));
    DelegateTheme dt9 = em.persist(new DelegateTheme(deleg3, economia));
    DelegateTheme dt10 = em.persist(new DelegateTheme(deleg1, impostos));
    c6.addDelegateTheme(dt5);
    c6.addDelegateTheme(dt6);
    c6.addDelegateTheme(dt7);
    c6.addDelegateTheme(dt8);
    c6.addDelegateTheme(dt9);
    c6.addDelegateTheme(dt10);

    // c7 saudePoll vote should be NEGATIVE (deleg3), economiaPoll vote should be NEGATIVE (deleg2)
    DelegateTheme dt11 = em.persist(new DelegateTheme(deleg3, saude));
    DelegateTheme dt12 = em.persist(new DelegateTheme(deleg2, orcamento));
    DelegateTheme dt13 = em.persist(new DelegateTheme(deleg3, profissionalSaude));
    DelegateTheme dt14 = em.persist(new DelegateTheme(deleg3, economia));
    c7.addDelegateTheme(dt11);
    c7.addDelegateTheme(dt12);
    c7.addDelegateTheme(dt13);
    c7.addDelegateTheme(dt14);

    // saudePoll and economiaPoll should not have votes
    DelegateTheme dt15 = em.persist(new DelegateTheme(deleg2, gastos));
    c8.addDelegateTheme(dt15);

    assertEquals(PollStatus.ACTIVE, saudePoll.getStatus());
    assertEquals(3, saudePoll.getNumPositiveVotes());
    assertEquals(3, saudePoll.getNumNegativeVotes());

    assertEquals(PollStatus.ACTIVE, economiaPoll.getStatus());
    assertEquals(4, economiaPoll.getNumPositiveVotes());
    assertEquals(2, economiaPoll.getNumNegativeVotes());

    // Calling the method being tested
    closePollsHandler.closePolls();

    // Checking that the polls has been closed and the vote counts are correct
    assertEquals(PollStatus.REJECTED, saudePoll.getStatus());
    assertEquals(3, saudePoll.getNumPositiveVotes());
    assertEquals(6, saudePoll.getNumNegativeVotes());

    assertEquals(PollStatus.APPROVED, economiaPoll.getStatus());
    assertEquals(6, economiaPoll.getNumPositiveVotes());
    assertEquals(3, economiaPoll.getNumNegativeVotes());
  }
}
