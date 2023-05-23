package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.VoteActivePollsHandler;

/**
 * Use case J.
 *
 * <p>Service class for that deals with vote operations. It delegates the operations to the {@link
 * VoteActivePollsHandler}.
 */
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
   * @param pollTitle The poll title to search for the actual Poll.
<<<<<<< HEAD
   * @param voterCitizenCardNumber The citizen card number.
   * @return The Delegate vote in the form of a string.
=======
   * @param voterCc The Citizen cc.
   * @return The Delegate vote in the form of a string. Returns null if there was no valid delegate
   *     found for citizen
>>>>>>> api_rest
   * @throws ApplicationException If no Poll or Citizen are found.
   */
  public VoteType checkDelegateVote(String pollTitle, Integer voterCitizenCardNumber)
      throws ApplicationException {
    return voteActivePollsHandler.checkDelegateVote(pollTitle, voterCitizenCardNumber);
  }

  /**
   * Vote on a Poll using the citizen card number and VoteType.
   *
   * @param pollTitle The title to search for the actual Poll.
   * @param voterCitizenCardNumber The citizen card number.
   * @param option The VoteType of the Citizen.
   * @throws ApplicationException If no Poll or Citizen are found.
   */
  public void vote(String pollTitle, Integer voterCitizenCardNumber, VoteType option)
      throws ApplicationException {
    voteActivePollsHandler.vote(pollTitle, voterCitizenCardNumber, option);
  }
}
