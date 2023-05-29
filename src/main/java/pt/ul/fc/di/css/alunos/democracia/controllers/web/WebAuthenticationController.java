package pt.ul.fc.di.css.alunos.democracia.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAuthenticationController {

  /**
   * Redirects the HTTP request to the authentication page.
   *
   * @return the name of the Thymeleaf template to render.
   */
  @GetMapping("/")
  public String redirectToAuth() {
    return "redirect:/authentication";
  }

  /**
   * Returns the authentication page.
   *
   * @return the name of the Thymeleaf template to render.
   */
  @GetMapping("/authentication")
  public String showDashboard() {
    return "authentication";
  }
}
