package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.VoteActivePollsHandler;

@Service
public class VoteActivePollsService {

  private final VoteActivePollsHandler voteActivePollsHandler;

  @Autowired
  public VoteActivePollsService(VoteActivePollsHandler voteActivePollsHandler) {
    this.voteActivePollsHandler = voteActivePollsHandler;
  }

  /**
   * Gets a List of all active Poll DTOs.
   *
   * @return A list containing all active Poll DTOs.
   */
  public List<PollDTO> getActivePolls() {
    return voteActivePollsHandler.getActivePolls();
  }

  /**
   * Checks a Delegate vote.
   *
   * @param pollDTO The pollDTO with the title to search for the actual Poll.
   * @param voterCc The Citizen cc.
   * @return The Delegate vote in the form of a string.
   * @throws ApplicationException If no Poll or Citizen are found.
   */
  public VoteType checkDelegateVote(PollDTO pollDTO, Integer voterCc) throws ApplicationException {
    return voteActivePollsHandler.checkDelegateVote(pollDTO, voterCc);
  }

  /**
   * Vote on a Poll using the Citizen cc and VoteType.
   *
   * @param pollDTO The pollDTO with the title to search for the actual Poll.
   * @param voterCc The Citizen cc.
   * @param option The VoteType of the Citizen.
   * @throws ApplicationException If no Poll or Citizen are found.
   */
  public void vote(PollDTO pollDTO, Integer voterCc, VoteType option) throws ApplicationException {
    voteActivePollsHandler.vote(pollDTO, voterCc, option);
  }
}
