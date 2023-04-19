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

  @Test
  void assignVotesAndClosePollsTest() {
    // Creating proposer and delegates
    Delegate proposer = em.persist(new Delegate("proposer", 0));
    Delegate deleg1 = em.persist(new Delegate("d1", 1));
    Delegate deleg2 = em.persist(new Delegate("d2", 2));
    Delegate deleg3 = em.persist(new Delegate("d3", 3));

    // Creating themes and subthemes
    Theme saude = em.persist(new Theme("saude", null));
    Theme profissionalSaude = em.persist(new Theme("profissional saude", saude));
    Theme medicos = em.persist(new Theme("medicos", profissionalSaude));
    Theme hospital = em.persist(new Theme("hospital", saude));
    Theme remedios = em.persist(new Theme("remedios", hospital));
    Theme macas = em.persist(new Theme("macas", hospital));

    // Creating a poll with a bill as the subject
    Poll poll =
        em.persist(
            new Poll(
                new Bill(
                    "bTitle", "desc", null, LocalDate.now().minusDays(1), proposer, remedios)));

    // Creating citizens who voted and adding their votes to the poll
    Citizen c1 = em.persist(new Citizen("c1", 55));
    Citizen c2 = em.persist(new Citizen("c2", 56));
    poll.addPrivateVoter(c1, VoteType.POSITIVE);
    poll.addPrivateVoter(c2, VoteType.NEGATIVE);

    // Creating citizens who did not vote
    Citizen c3 = em.persist(new Citizen("c3", 57));
    Citizen c4 = em.persist(new Citizen("c4", 58));
    Citizen c5 = em.persist(new Citizen("c5", 59));

    // Adding public voters and their votes to the poll
    poll.addPublicVoter(deleg1, VoteType.NEGATIVE);
    poll.addPublicVoter(deleg2, VoteType.POSITIVE);
    poll.addPublicVoter(deleg3, VoteType.NEGATIVE);

    // Assigning delegates to themes for citizens who did not vote
    DelegateTheme dt1 = em.persist(new DelegateTheme(deleg1, saude));
    DelegateTheme dt2 = em.persist(new DelegateTheme(deleg1, medicos));
    DelegateTheme dt3 = em.persist(new DelegateTheme(deleg1, hospital));
    c3.addDelegateTheme(dt1);
    c3.addDelegateTheme(dt2);
    c3.addDelegateTheme(dt3);

    DelegateTheme dt4 = em.persist(new DelegateTheme(deleg2, medicos));
    DelegateTheme dt5 = em.persist(new DelegateTheme(deleg3, saude));
    DelegateTheme dt6 = em.persist(new DelegateTheme(deleg1, profissionalSaude));
    c4.addDelegateTheme(dt4);
    c4.addDelegateTheme(dt5);
    c4.addDelegateTheme(dt6);

    // Calling the method being tested
    closePollsHandler.closePolls();

    // Checking that the poll has been closed and the vote counts are correct
    assertEquals(PollStatus.REJECTED, poll.getStatus());
    assertEquals(3, poll.getNumPositiveVotes());
    assertEquals(5, poll.getNumNegativeVotes());
  }
}
