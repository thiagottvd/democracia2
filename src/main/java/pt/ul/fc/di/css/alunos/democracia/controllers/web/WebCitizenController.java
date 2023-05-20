package pt.ul.fc.di.css.alunos.democracia.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.services.ChooseDelegateService;

/**
 * The WebCitizenController class is a controller that handles HTTP requests related to citizens. It
 * generates and returns an HTML webpage to the client, and exposes an endpoint for choosing a delegate
 * for a particular theme for a citizen.
 */
@Controller
public class WebCitizenController {

    private static final String CHOOSE_DELEGATE_VIEW = "choose_delegate";
    private static final String CHOOSE_DELEGATE_SUCCESS = "Sucesso ao escolher delegado!";
    private static final String CITIZEN_NOT_FOUND_VIEW = "error/citizen_404";

    private final ChooseDelegateService chooseDelegateService;

    /**
     * Constructs a new WebCitizenController with the given ChooseDelegateService.
     *
     * @param chooseDelegateService the ChooseDelegateService to use for choosing a delegate
     *                              for a particular theme for a citizen.
     */
    @Autowired
    public WebCitizenController(ChooseDelegateService chooseDelegateService) {
        this.chooseDelegateService = chooseDelegateService;
    }

    /**
     * Handles the GET request for choosing a delegate-theme.
     * Retrieves the list of delegates and themes from the service layer
     * and adds them to the model. Returns the logical view name of the
     * Thymeleaf template to be rendered for choosing a delegate-theme.
     *
     * @param model             the model to be populated with data
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
     * Handles the PATCH request for choosing a delegate.
     * Attempts to choose a delegate for the specified citizen with the given delegate and theme.
     * If any application exception occurs, it adds the error message to the model and returns to
     * the choose delegate view page. If the operation is successful, it adds a success message
     * to the model and redirects back to the chooseDelegate GET page.
     *
     * @param model             the model to be populated with data
     * @param citizenCardNumber the citizen card number
     * @param delegate          the chosen delegate
     * @param theme             the chosen theme
     * @return the filled HTML page for the choose delegate view or a 404 page if the citizen is not found.
     */
    @PatchMapping("/citizens/{citizenCardNumber}/choose-delegate")
    public String chooseDelegateAction(final Model model, @PathVariable("citizenCardNumber") Integer citizenCardNumber, @RequestParam("delegate") Integer delegate, @RequestParam("theme") String theme) {
        try {
            chooseDelegateService.chooseDelegate(citizenCardNumber, theme, delegate);
        } catch (CitizenNotFoundException e) {
            return CITIZEN_NOT_FOUND_VIEW;
        } catch (ApplicationException e) {
            model.addAttribute("error", e.getMessage());
            return chooseDelegate(model, citizenCardNumber);
        }
        model.addAttribute("success", CHOOSE_DELEGATE_SUCCESS);
        return chooseDelegate(model, citizenCardNumber);
    }
}
