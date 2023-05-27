package pt.ul.fc.di.css.alunos.democracia.controllers.web;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.DuplicateDelegateThemeException;
import pt.ul.fc.di.css.alunos.democracia.services.ChooseDelegateService;
import pt.ul.fc.di.css.alunos.democracia.services.VoteActivePollsService;

/**
 * The WebCitizenController class is a controller that handles HTTP requests related to citizens. It
 * generates and returns an HTML webpage to the client, and exposes an endpoint for choosing a
 * delegate for a particular theme for a citizen.
 */
@Controller
public class WebCitizenController {

  private static final String CHOOSE_DELEGATE_VIEW = "choose_delegate";
  private static final String VOTE_VIEW = "vote";
  private static final String VOTE_UNAVAILABLE = "Voto indisponível.";
  private static final String CHOOSE_DELEGATE_SUCCESS = "Sucesso ao escolher delegado!";
  private static final String VOTE_SUCCESS = "Voto computado com sucesso!";
  private static final String CITIZEN_NOT_FOUND_VIEW = "error/citizen_404";

  private final ChooseDelegateService chooseDelegateService;
  private final VoteActivePollsService voteActivePollsService;

  /**
   * Constructs a new WebCitizenController with the given ChooseDelegateService and
   * VoteActivePollsService.
   *
   * @param chooseDelegateService the ChooseDelegateService to use for choosing a delegate for a
   *     particular theme for a citizen.
   * @param voteActivePollsService the VoteActivePollsService to use for voting.
   */
  @Autowired
  public WebCitizenController(
      ChooseDelegateService chooseDelegateService, VoteActivePollsService voteActivePollsService) {
    this.chooseDelegateService = chooseDelegateService;
    this.voteActivePollsService = voteActivePollsService;
  }

  /**
   * Handles the GET request for choosing a delegate-theme. Retrieves the list of delegates and
   * themes from the service layer and adds them to the model. Returns the logical view name of the
   * Thymeleaf template to be rendered for choosing a delegate-theme.
   *
   * @param model the model to be populated with data
   * @param citizenCardNumber the citizen card number
   * @return the filled HTML page for the choose delegate view.
   */
  @GetMapping("/citizens/{citizenCardNumber}/choose-delegate")
  public String chooseDelegate(final Model model, @PathVariable Integer citizenCardNumber) {
    model.addAttribute("delegates", chooseDelegateService.getDelegates());
    model.addAttribute(
        "themes", chooseDelegateService.getThemes().stream().map(ThemeDTO::getDesignation));
    return CHOOSE_DELEGATE_VIEW;
  }

  /**
   * Handles the PATCH request for choosing a delegate. Attempts to choose a delegate for the
   * specified citizen with the given delegate and theme. If any application exception occurs, it
   * adds the error message to the model and returns to the choose delegate view page. If the
   * operation is successful, it adds a success message to the model and redirects back to the
   * chooseDelegate GET page.
   *
   * @param model the model to be populated with data
   * @param citizenCardNumber the citizen card number
   * @param delegate the chosen delegate
   * @param theme the chosen theme
   * @return the filled HTML page for the choose delegate view or a 404 page if the citizen is not
   *     found.
   */
  @PatchMapping("/citizens/{citizenCardNumber}/choose-delegate")
  public String chooseDelegateAction(
      final Model model,
      @PathVariable("citizenCardNumber") Integer citizenCardNumber,
      @RequestParam("delegate") Integer delegate,
      @RequestParam("theme") String theme) {
    try {
      chooseDelegateService.chooseDelegate(delegate, theme, citizenCardNumber);
    } catch (CitizenNotFoundException e) {
      return CITIZEN_NOT_FOUND_VIEW;
    } catch (DuplicateDelegateThemeException e) {
      model.addAttribute("success", e.getMessage());
      return chooseDelegate(model, citizenCardNumber);
    } catch (ApplicationException e) {
      model.addAttribute("error", e.getMessage());
      return chooseDelegate(model, citizenCardNumber);
    }
    model.addAttribute("success", CHOOSE_DELEGATE_SUCCESS);
    return chooseDelegate(model, citizenCardNumber);
  }

  @GetMapping("/citizens/{citizenCardNumber}/vote")
  public String vote(
      final Model model, @PathVariable Integer citizenCardNumber, HttpSession session) {
    List<PollDTO> activePolls = voteActivePollsService.getActivePolls();
    session.setAttribute("activePolls", activePolls);
    model.addAttribute("activePolls", activePolls);
    return VOTE_VIEW;
  }

  @GetMapping("/citizens/{citizenCardNumber}/vote-show-delegate-vote")
  public String voteShowDelegateVote(
      final Model model,
      @PathVariable Integer citizenCardNumber,
      @RequestParam("selectedPollId") Long selectedPollId,
      HttpSession session) {
    try {
      VoteType voteType =
          voteActivePollsService.checkDelegateVote(selectedPollId, citizenCardNumber);
      String strVote = (voteType != null) ? mapVoteMessage(voteType) : VOTE_UNAVAILABLE;
      model.addAttribute("delegateVote", strVote);

      List<PollDTO> activePolls = (List<PollDTO>) session.getAttribute("activePolls");
      model.addAttribute("activePolls", activePolls);

      session.setAttribute("selectedPollId", selectedPollId);
    } catch (ApplicationException e) {
      model.addAttribute("error", e.getMessage());
      return VOTE_VIEW;
    }
    return VOTE_VIEW;
  }

  @PatchMapping("/citizens/{citizenCardNumber}/vote")
  public String voteAction(
      Model model,
      @PathVariable("citizenCardNumber") Integer citizenCardNumber,
      @RequestParam("voteType") Integer intVoteType,
      HttpSession session) {
    VoteType voteType = (intVoteType == 0) ? VoteType.POSITIVE : VoteType.NEGATIVE;
    Long selectedPollId = (Long) session.getAttribute("selectedPollId");
    try {
      voteActivePollsService.vote(selectedPollId, citizenCardNumber, voteType);
    } catch (ApplicationException e) {
      model.addAttribute("error", e.getMessage());
      return vote(model, citizenCardNumber, session);
    }

    model.addAttribute("success", VOTE_SUCCESS);
    return vote(model, citizenCardNumber, session);
  }

  private String mapVoteMessage(VoteType voteType) {
    switch (voteType) {
      case POSITIVE:
        return "Positivo";
      case NEGATIVE:
        return "Negativo";
      default:
        return VOTE_UNAVAILABLE;
    }
  }
}
