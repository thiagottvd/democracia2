package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.*;

/**
 * Handles use case K.
 *
 * <p>Handles the closure of expired polls and distributes votes from citizens that had active
 * representation
 */
@Component
public class ClosePollsHandler {

  private final PollCatalog pollCatalog;
  private final CitizenCatalog citizenCatalog;

  /**
   * Constructs a new ClosePollsHandler object.
   *
   * @param pollCatalog the poll catalog to be used by the handler.
   * @param citizenCatalog the citizen catalog to be used by the handler.
   */
  @Autowired
  public ClosePollsHandler(PollCatalog pollCatalog, CitizenCatalog citizenCatalog) {
    this.pollCatalog = pollCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  /** Main Body of handler. Gets all expired polls and processes them */
  @Scheduled(fixedRate = 60000)
  public void closePolls() {
    List<Poll> expiredPolls = pollCatalog.getExpiredPolls();
    getNonVotersAndAssignVotes(expiredPolls);
    setPollsStatus(expiredPolls);
  }

  /**
   * Finds all citizens that did not vote in each expired poll to check if their delegate did, and
   * if so, assigns their votes to the most representative delegate if there is any.
   *
   * @param expiredPolls Polls that are expired
   */
  private void getNonVotersAndAssignVotes(List<Poll> expiredPolls) {
    for (Poll expiredPoll : expiredPolls) {
      List<Citizen> voters = expiredPoll.getAllVoters();
      List<Citizen> nonVoters = citizenCatalog.findAllNonVoters(voters);
      for (Citizen nonVoter : nonVoters) {
        assignVoteForCitizen(nonVoter, expiredPoll);
      }
    }
  }

  /**
   * Core Logic of handler. Auxiliary method that checks if a nonvoter citizen has a representative
   * delegate. It keeps checking again in a new theme that is the parent of current poll theme if it
   * does not find a match.
   *
   * <p>Stops if current theme has no parent (did not find valid delegate that represent voter), or
   * if it finds a valid delegate representing the voter.
   *
   * <p>When it finds a valid representative in the theme branch/cycle increments votes and closes
   * poll.
   *
   * <p>If there is no delegates representing the citizen, no votes are added to the poll.
   *
   * @param nonVoter the citizen that did not vote but could be represented by a delegate in a
   *     theme.
   * @param expiredPoll the Poll that expired.
   */
  private void assignVoteForCitizen(Citizen nonVoter, Poll expiredPoll) {
    Theme theme = expiredPoll.getAssociatedBill().getTheme();

    while (theme != null) {
      for (DelegateTheme delegateTheme : nonVoter.getDelegateThemes()) {
        Delegate delegate = delegateTheme.getDelegate();
        Theme otherTheme = delegateTheme.getTheme();
        if (expiredPoll.getPublicVoters().containsKey(delegate) && theme.equals(otherTheme)) {
          incVoteInPoll(expiredPoll, delegate);
          return;
        }
      }
      theme = theme.getParentTheme();
    }
  }

  /**
   * Gets the public vote of the delegate and increments it for the given poll.
   *
   * @param expiredPoll the poll that expired.
   * @param delegate the delegate that represents someone.
   */
  private void incVoteInPoll(Poll expiredPoll, Delegate delegate) {
    switch (expiredPoll.getPublicVoters().get(delegate)) {
      case POSITIVE:
        expiredPoll.incPositiveVotes();
        break;
      case NEGATIVE:
        expiredPoll.incNegativeVotes();
        break;
    }
  }

  /**
   * Closes all expired polls by setting their status as approved if the number of positive votes is
   * greater than the number of negative votes, or rejected otherwise.
   *
   * @param expiredPolls the polls that expired.
   */
  private void setPollsStatus(List<Poll> expiredPolls) {
    expiredPolls.forEach(
        poll ->
            poll.setStatus(
                poll.getNumPositiveVotes() > poll.getNumNegativeVotes()
                    ? PollStatus.APPROVED
                    : PollStatus.REJECTED));
  }
}
