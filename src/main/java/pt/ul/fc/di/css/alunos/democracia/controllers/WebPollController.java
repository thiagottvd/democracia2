package pt.ul.fc.di.css.alunos.democracia.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.services.ConsultBillsService;
import pt.ul.fc.di.css.alunos.democracia.services.ListActivePollsService;

@Controller
public class WebPollController {

  private Logger logger = LoggerFactory.getLogger(WebPollController.class);

  private final ListActivePollsService listActivePollsService;
  private final ConsultBillsService consultBillsService;

  @Autowired
  public WebPollController(
      ListActivePollsService listActivePollsService, ConsultBillsService consultBillsService) {
    this.listActivePollsService = listActivePollsService;
    this.consultBillsService = consultBillsService;
  }

  @GetMapping("/polls/active")
  public String getActivePolls(final Model model) {
    List<PollDTO> activePolls = listActivePollsService.getActivePolls();
    model.addAttribute("activePolls", activePolls);
    return "active_polls_list";
  }
}
