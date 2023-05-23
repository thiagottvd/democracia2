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
import pt.ul.fc.di.css.alunos.democracia.exceptions.InvalidVoteTypeException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.PollNotFoundException;

/**
 * Handles use case J.
 *
 * <p>Handler that handles votes.
 */
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
        .map(poll -> new PollDTO(poll.getId(), poll.getAssociatedBill().getTitle()))
        .collect(Collectors.toList());
  }

  /**
   * Checks a Delegate vote.
   *
   * @param pollTitle The title to search for the actual Poll.
<<<<<<< HEAD
   * @param voterCitizenCardNumber The citizen card number.
   * @return The Delegate vote in the form of a string.
=======
   * @param voterCc The Citizen cc.
   * @return The Delegate vote in the form of a string. Returns null if citizen has no delegate
>>>>>>> api_rest
   * @throws ApplicationException If no Poll or Citizen are found.
   */
  public VoteType checkDelegateVote(String pollTitle, Integer voterCitizenCardNumber)
      throws ApplicationException {
    Citizen citizen = validateCitizen(voterCitizenCardNumber);
    Poll poll = validatePoll(pollTitle);
    Theme theme = poll.getAssociatedBill().getTheme();
    List<DelegateTheme> delegateThemesList = citizen.getDelegateThemes();
    Delegate delegate = findDelegateForTheme(theme, delegateThemesList);
    return delegate != null ? poll.getDelegateVote(delegate) : null;
  }

  /**
   * Searches the list of delegate themes for the first delegate that can vote on the given theme.
   *
   * @param theme The theme to search for.
   * @param delegateThemesList The list of delegate themes to search in.
   * @return The first delegate that can vote on the theme, or null if no delegate is found.
   */
  private Delegate findDelegateForTheme(Theme theme, List<DelegateTheme> delegateThemesList) {
    for (Theme currentTheme = theme;
        currentTheme != null;
        currentTheme = currentTheme.getParentTheme()) {
      for (DelegateTheme delegateTheme : delegateThemesList) {
        if (delegateTheme.checkTheme(currentTheme)) {
          return delegateTheme.getDelegate();
        }
      }
    }
    return null;
  }

  /**
   * Vote on a Poll using the citizen card number and VoteType.
   *
   * @param pollTitle The title to search for the actual Poll.
   * @param voterCitizenCardNumber The citizen card number.
   * @param option The VoteType of the Citizen.
   * @throws ApplicationException If no Poll or Citizen are found or if the vote type is invalid.
   */
  public void vote(String pollTitle, Integer voterCitizenCardNumber, VoteType option)
      throws ApplicationException {
    if (option == null
        || (!option.equals(VoteType.POSITIVE) && !option.equals(VoteType.NEGATIVE))) {
      throw new InvalidVoteTypeException("The vote type is invalid.");
    }
    Citizen citizen = validateCitizen(voterCitizenCardNumber);
    Poll poll = validatePoll(pollTitle);
    poll.addVoter(citizen, option);
  }

  /**
   * Aux method to validate if the Citizen exists, and if so returns the citizen.
   *
   * @param voterCitizenCardNumber The given citizen card number.
   * @return The Citizen if found.
   * @throws CitizenNotFoundException If the citizen is not found.
   */
  private Citizen validateCitizen(Integer voterCitizenCardNumber) throws CitizenNotFoundException {
    Optional<Citizen> citizen =
        citizenCatalog.getCitizenByCitizenCardNumber(voterCitizenCardNumber);
    if (citizen.isEmpty()) {
      throw new CitizenNotFoundException(
          "Citizen with id: " + voterCitizenCardNumber + " not found.");
    }
    return citizen.get();
  }

  /**
   * Aux method to validate if the Poll exists, and if so returns the poll.
   *
   * @param pollTitle The title used to search the actual Poll.
   * @return The Poll if found.
   * @throws PollNotFoundException If the poll is not found.
   */
  private Poll validatePoll(String pollTitle) throws PollNotFoundException {
    Optional<Poll> poll = pollCatalog.getPollByTitle(pollTitle);
    if (poll.isEmpty()) {
      throw new PollNotFoundException("Poll with title: " + pollTitle + " not found.");
    }
    return poll.get();
  }
}
