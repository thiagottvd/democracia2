package pt.ul.fc.di.css.alunos.democracia.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** This class serves as a controller for handling HTTP requests related to the web dashboard. */
@Controller
public class WebDashboardController {

  /**
   * Redirects the HTTP request to the dashboard page.
   *
   * @return the name of the Thymeleaf template to render.
   */
  @GetMapping("/")
  public String redirectToDashboard() {
    return "redirect:/dashboard";
  }

  /**
   * Returns the dashboard page.
   *
   * @return the name of the Thymeleaf template to render.
   */
  @GetMapping("/dashboard")
  public String showDashboard() {
    return "dashboard";
  }
}
