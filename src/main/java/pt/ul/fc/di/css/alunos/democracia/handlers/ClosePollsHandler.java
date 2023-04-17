package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.*;

@Component
public class ClosePollsHandler {

  private final PollCatalog pollCatalog;
  private final CitizenCatalog citizenCatalog;

  @Autowired
  public ClosePollsHandler(PollCatalog pollCatalog, CitizenCatalog citizenCatalog) {
    this.pollCatalog = pollCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  public void closePolls() {
    List<Poll> expiredPolls = pollCatalog.getExpiredPolls();
    getNonVotersAndAssignVotes(expiredPolls);
    setPollsStatus(expiredPolls);
  }

  private void getNonVotersAndAssignVotes(List<Poll> expiredPolls) {
    for (Poll expiredPoll : expiredPolls) {
      List<Citizen> voters = expiredPoll.getAllVoters();
      List<Citizen> nonVoters = citizenCatalog.findAllNonVoters(voters);
      for (Citizen nonVoter : nonVoters) {
        assignVoteForCitizen(nonVoter, expiredPoll);
      }
    }
  }

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

  private void setPollsStatus(List<Poll> expiredPolls) {
    expiredPolls.forEach(
        poll ->
            poll.setStatus(
                poll.getNumPositiveVotes() > poll.getNumNegativeVotes()
                    ? PollStatus.APPROVED
                    : PollStatus.REJECTED));
  }
}
