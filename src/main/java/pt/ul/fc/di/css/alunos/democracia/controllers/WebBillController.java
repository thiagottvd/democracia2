package pt.ul.fc.di.css.alunos.democracia.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.*;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.InvalidDateException;
import pt.ul.fc.di.css.alunos.democracia.services.ConsultBillsService;
import pt.ul.fc.di.css.alunos.democracia.services.ProposeBillService;

/**
 * The WebBillController class is a controller that handles HTTP requests related to bills. It
 * generates and returns an HTML webpage to the client, and exposes endpoints for proposing bills,
 * consulting bills, and viewing the details of a specific bill.
 */
@Controller
public class WebBillController {

  private static final String PROPOSE_BILL_VIEW = "propose_bill";
  private static final String BILL_DETAILS_VIEW = "bill_details";
  private static final String BILL_NOT_FOUND_VIEW = "error/bill_404";
  private static final String PDF_READING_ERROR_VIEW = "error/pdf_500";
  private static final String REDIRECT_TO_BILL_DETAIL_VIEW = "redirect:/bills/";
  private static final String TITLE_FIELD = "title";
  private static final String DESCRIPTION_FIELD = "description";
  private static final String FILE_FIELD = "fileData";
  private static final String FILE_MAX_CAPACITY = "fileMaxCapacity";
  private static final String TITLE_LENGTH_ERROR = "O título deve ter entre 3 e 125 caracteres.";
  private static final String DESCRIPTION_LENGTH_ERROR =
      "A descrição deve ter entre 3 e 250 caracteres.";
  private static final String FILE_REQUIRED_ERROR = "É necessário carregar um ficheiro PDF.";
  private static final String INVALID_EXPIRATION_DATE_ERROR =
      "A data de expiração deve ser igual ou posterior ao dia de hoje.";
  private static final String GENERIC_ERROR = "Ocorreu um erro inesperado.";
  private static final String FILE_MAX_CAPACITY_ERROR =
      "A capacidade máxima do ficheiro PDF é de 10MB.";

  private final Logger logger = LoggerFactory.getLogger(WebPollController.class);
  private final ProposeBillService proposeBillService;
  private final ConsultBillsService consultBillsService;

  /**
   * Constructs a new WebBillController with the given ProposeBillService and ConsultBillsService.
   *
   * @param proposeBillService the ProposeBillService to use for proposing new bills.
   * @param consultBillsService the ConsultBillsService to use for consulting existing bills.
   */
  @Autowired
  public WebBillController(
      ProposeBillService proposeBillService, ConsultBillsService consultBillsService) {
    this.proposeBillService = proposeBillService;
    this.consultBillsService = consultBillsService;
  }

  /**
   * Handles GET requests to the '/bills/propose' endpoint, which displays a form for proposing a
   * new bill. Populates the model with the bill data and a list of available themes to be displayed
   * on the form.
   *
   * <p>Returns the Thymeleaf template for the page where a bill can be proposed.
   *
   * @param model the model to be used by Thymeleaf to render the view.
   * @return the Thymeleaf template for the proposal bill page.
   */
  @GetMapping("/bills/propose")
  public String proposeBill(final Model model) {
    BillDTO billDTO = new BillDTO();
    // TODO: It is a mock, it must be the citizen card number of the user who is proposing the bill.
    // billDTO.setDelegateCitizenCardNumber(1);
    // logger.debug(billDTO.getDelegateCitizenCardNumber().toString());
    model.addAttribute("bill", billDTO);
    model.addAttribute(
        "themes", proposeBillService.getThemes().stream().map(ThemeDTO::getDesignation));
    return PROPOSE_BILL_VIEW;
  }

  /**
   * Handles POST requests to the '/bills' endpoint, which processes a new bill proposal. Validates
   * the bill data and uploads the bill main content PDF file to the server.
   *
   * @param model the model to be used by Thymeleaf to render the view.
   * @param bDTO the BillDTO object containing the bill data from the form.
   * @param bindingResult the result of the validation process for the BillDTO object.
   * @param file the PDF file containing the bill main content.
   * @return the Thymeleaf template for the proposal bill page in case of errors, or a redirect to
   *     the bill detail view in case of success.
   */
  @PostMapping("/bills")
  public String proposeBillAction(
      final Model model,
      @Valid BillDTO bDTO,
      BindingResult bindingResult,
      @RequestParam("file") MultipartFile file) {

    if (file.isEmpty() || !Objects.equals(file.getContentType(), MediaType.APPLICATION_PDF_VALUE)) {
      setupModelOnErrorForProposeBillAction(model, "fileData");
      return PROPOSE_BILL_VIEW;
    }

    if (bindingResult.hasErrors()) {
      setupModelOnErrorForProposeBillAction(
          model, Objects.requireNonNull(bindingResult.getFieldError()).getField());
      return PROPOSE_BILL_VIEW;
    }

    try {
      BillDTO billDTO =
          proposeBillService.proposeBill(
              bDTO.getTitle(),
              bDTO.getDescription(),
              file.getBytes(),
              bDTO.getExpirationDate(),
              bDTO.getThemeDesignation(),
              1);
      logger.debug("INFO: Bill proposed. Reference added to the database.");
      return REDIRECT_TO_BILL_DETAIL_VIEW + billDTO.getId();
    } catch (ApplicationException | IOException e) {
      setupModelOnExceptionForProposeBillAction(model, e);
      return PROPOSE_BILL_VIEW;
    }
  }

  /**
   * Handles GET requests to the '/bills/{billId}' endpoint, which retrieves the details of a
   * specific bill.
   *
   * @param model the model to be used by Thymeleaf to render the view.
   * @param billId the ID of the bill to retrieve.
   * @return the Thymeleaf template for the bill detail view, or a template for a not-found page if
   *     the bill doesn't exist.
   */
  @GetMapping("/bills/{billId}")
  public String getBillDetails(final Model model, @PathVariable Long billId) {
    try {
      BillDTO billDTO = consultBillsService.getBillDetails(billId);
      model.addAttribute("bill", billDTO);
      return BILL_DETAILS_VIEW;
    } catch (ApplicationException e) {
      return BILL_NOT_FOUND_VIEW;
    }
  }

  /**
   * Handles GET requests to the '/bills/{billId}/pdf' endpoint, which displays the PDF file of a
   * specific bill. Retrieves the file data from the database and writes it to the response as a PDF
   * content type.
   *
   * @param billId the ID of the bill whose PDF file is being requested.
   * @param response the HttpServletResponse object used to write the PDF content to the response.
   * @return nothing.
   */
  @GetMapping(value = "/bills/{billId}/pdf")
  public String displayPDF(@PathVariable Long billId, HttpServletResponse response) {
    BillDTO billDTO;
    try {
      billDTO = consultBillsService.getBillDetails(billId);
    } catch (ApplicationException e) {
      return BILL_NOT_FOUND_VIEW;
    }
    response.setContentType(MediaType.APPLICATION_PDF_VALUE);
    InputStream inputStream = new ByteArrayInputStream(billDTO.getFileData());
    int nRead;
    try {
      while ((nRead = inputStream.read()) != -1) {
        response.getWriter().write(nRead);
      }
    } catch (IOException e) {
      return PDF_READING_ERROR_VIEW;
    }
    return null;
  }

  /**
   * Setups the model attributes in case of errors in the bill proposal action.
   *
   * @param model the model to be used by Thymeleaf to render the view.
   * @param field the name of the field where the error occurred.
   */
  private void setupModelOnErrorForProposeBillAction(final Model model, String field) {
    model.addAttribute("bill", new BillDTO());
    model.addAttribute(
        "themes", proposeBillService.getThemes().stream().map(ThemeDTO::getDesignation));
    model.addAttribute("error", getFieldErrorMessageForProposeBillAction(field));
  }

  /**
   * Setups the model attributes in case of exceptions in the bill proposal action.
   *
   * @param model the model to be used by Thymeleaf to render the view.
   * @param exception the exception that occurred during the bill proposal.
   */
  private void setupModelOnExceptionForProposeBillAction(final Model model, Exception exception) {
    model.addAttribute("bill", new BillDTO());
    model.addAttribute(
        "themes", proposeBillService.getThemes().stream().map(ThemeDTO::getDesignation));
    model.addAttribute("error", getExceptionErrorMessageForProposeBillAction(exception));
  }

  /**
   * Returns the error message to be displayed in the view, based on the type of exception that
   * occurred.
   *
   * @param exception the exception that occurred during the bill proposal.
   * @return the error message to be displayed in the view.
   */
  private String getExceptionErrorMessageForProposeBillAction(Exception exception) {
    if (exception.getClass() == InvalidDateException.class) {
      return INVALID_EXPIRATION_DATE_ERROR;
    }
    return GENERIC_ERROR;
  }

  /**
   * Returns the error message to be displayed in the view, based on the field where the validation
   * error occurred.
   *
   * @param field the field where the validation error occurred during the bill proposal.
   * @return the error message to be displayed in the view.
   */
  private String getFieldErrorMessageForProposeBillAction(String field) {
    switch (field) {
      case TITLE_FIELD:
        return TITLE_LENGTH_ERROR;
      case DESCRIPTION_FIELD:
        return DESCRIPTION_LENGTH_ERROR;
      case FILE_FIELD:
        return FILE_REQUIRED_ERROR;
      case FILE_MAX_CAPACITY:
        return FILE_MAX_CAPACITY_ERROR;
      default:
        return GENERIC_ERROR;
    }
  }

  /**
   * This class is a controller advice that handles the MaxUploadSizeExceededException thrown when a
   * user tries to upload a file that exceeds the maximum allowed size. It returns a view that
   * displays an error message and sets up the model to display the "propose bill" page with the
   * appropriate error message and data.
   */
  @ControllerAdvice
  private class FileMaxUploadErrorHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    private String handleMaxSizeException(final Model model) {
      setupModelOnErrorForProposeBillAction(model, FILE_MAX_CAPACITY);
      model.addAttribute(
          "themes", proposeBillService.getThemes().stream().map(ThemeDTO::getDesignation));
      return PROPOSE_BILL_VIEW;
    }
  }
}
