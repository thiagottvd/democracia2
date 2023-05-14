package pt.ul.fc.di.css.alunos.democracia.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.services.ListActivePollsService;

/**
 * Controller responsible for mapping requests related to Polls, generating and rendering Thymeleaf
 * templates on the server side, and returning the rendered templates as HTML pages.
 */
@Controller
public class WebPollController {

  private final ListActivePollsService listActivePollsService;

  /**
   * Constructor injection for ListActivePollsService.
   *
   * @param listActivePollsService the ListActivePollsService to inject.
   */
  @Autowired
  public WebPollController(ListActivePollsService listActivePollsService) {
    this.listActivePollsService = listActivePollsService;
  }

  /**
   * Maps requests to `/polls/active` and retrieves a list of active polls, then adds the list to
   * the model and returns the name of the Thymeleaf template to render.
   *
   * @param model the model to which the list of active polls will be added.
   * @return the name of the Thymeleaf template to render.
   */
  @GetMapping("/polls/active")
  public String getActivePolls(final Model model) {
    List<PollDTO> activePolls = listActivePollsService.getActivePolls();
    model.addAttribute("activePolls", activePolls);
    return "active_polls_list";
  }
}
