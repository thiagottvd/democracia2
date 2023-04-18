package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.*;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;

@Component
public class VoteActivePollsHandler {
  private final PollCatalog pollCatalog;
  private final CitizenCatalog citizenCatalog;

  /**
   * Constructor for the VoteActivePollsHandler class.
   *
   * @param pollCatalog The Polls Catalog.
   * @param citizenCatalog The Citizens Catalog.
   */
  @Autowired
  public VoteActivePollsHandler(PollCatalog pollCatalog, CitizenCatalog citizenCatalog) {
    this.pollCatalog = pollCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  /**
   * Gets a List of all active Poll DTOs.
   *
   * @return A list containing all active Poll DTOs.
   */
  public List<PollDTO> getActivePolls() {
    List<Poll> activePolls = pollCatalog.getPollsByStatusType(PollStatus.ACTIVE);
    return activePolls.stream()
        .map(poll -> new PollDTO(poll.getBill().getTitle()))
        .collect(Collectors.toList());
  }

  /**
   * Checks a Delegate vote.
   *
   * @param pollDTO The pollDTO with the title to search for the actual Poll.
   * @param voterCc The Citizen cc.
   * @return The Delegate vote in the form of a string.
   * @throws ApplicationException If no Poll or Citizen are found.
   */
  public VoteType checkDelegateVote(PollDTO pollDTO, int voterCc) throws ApplicationException {
    Citizen citizen = validateCitizen(voterCc).get();
    Poll poll = validatePoll(pollDTO);
    Theme theme = poll.getAssociatedBill().getTheme();
    List<DelegateTheme> delegateThemesList = citizen.getDelegateThemes();
    Delegate delegate = null;
    boolean hasFoundDelegate = false;
    while (theme != null && !hasFoundDelegate) {
      for (DelegateTheme delegateTheme : delegateThemesList) {
        if (delegateTheme.checkTheme(theme)) {
          delegate = delegateTheme.getDelegate();
          hasFoundDelegate = true;
          break;
        }
      }
      theme = theme.getParentTheme();
    }
    return delegate != null ? poll.getPublicVote(delegate) : null;
  }

  /**
   * Vote on a Poll using the Citizen cc and VoteType.
   *
   * @param pollDTO The pollDTO with the title to search for the actual Poll.
   * @param voterCc The Citizen cc.
   * @param option The VoteType of the Citizen.
   * @throws ApplicationException If no Poll or Citizen are found.
   */
  public void vote(PollDTO pollDTO, int voterCc, VoteType option) throws ApplicationException {
    if (option == null) {
      throw new ApplicationException("The vote type is invalid.");
    }
    Citizen citizen = validateCitizen(voterCc).get();
    Poll poll = validatePoll(pollDTO);
    poll.addPrivateVoter(citizen, option);
  }

  /**
   * Aux method to validate the Citizen.
   *
   * @param voterCc The given Citizen cc.
   * @return The Citizen if found.
   * @throws CitizenNotFoundException If no Citizen is found.
   */
  private Optional<Citizen> validateCitizen(int voterCc) throws CitizenNotFoundException {
    Optional<Citizen> citizen = citizenCatalog.getCitizenByCc(voterCc);
    if (citizen.isEmpty()) {
      throw new CitizenNotFoundException("Citizen with id: " + voterCc + " not found.");
    }
    return citizen;
  }

  /**
   * Aux method to validate the Poll.
   *
   * @param pollDTO The pollDTO with the title used to search the actual Poll.
   * @return The Poll if found.
   * @throws ApplicationException If no Poll is found.
   */
  private Poll validatePoll(PollDTO pollDTO) throws ApplicationException {
    Poll poll = pollCatalog.getPollByTitle(pollDTO.getTitle());
    if (poll == null) {
      throw new ApplicationException("Poll with title: " + pollDTO.getTitle() + " not found.");
    }
    return poll;
  }
}
