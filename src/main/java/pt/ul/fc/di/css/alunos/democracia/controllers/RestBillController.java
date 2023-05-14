package pt.ul.fc.di.css.alunos.democracia.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.*;
import pt.ul.fc.di.css.alunos.democracia.services.ConsultBillsService;
import pt.ul.fc.di.css.alunos.democracia.services.SupportBillService;

/**
 * This is a RESTful API controller that handles HTTP requests related to bills. The API provides
 * endpoints to retrieve open bills, get details of a specific bill, and add support of a citizen to
 * a given bill.
 */
@RestController()
@RequestMapping("api")
public class RestBillController {

  private final ConsultBillsService consultBillsService;
  private final SupportBillService supportBillService;

  /**
   * Constructs a new RestBillController with the specified ConsultBillsService and
   * SupportBillService.
   *
   * @param consultBillsService the service used to consult bills.
   * @param supportBillService the service used to support bills.
   */
  @Autowired
  public RestBillController(
      ConsultBillsService consultBillsService, SupportBillService supportBillService) {
    this.consultBillsService = consultBillsService;
    this.supportBillService = supportBillService;
  }

  /**
   * Retrieves a list of all open bills, returning only their ID and title. Other fields are either
   * empty or initialized with default values.
   *
   * @return a list of all open bills, represented as a list of BillDTO objects containing only the
   *     ID and title of each bill. Other fields are either empty or initialized with default
   *     values.
   */
  @GetMapping("/bills/open")
  public List<BillDTO> getOpenBills() {
    return consultBillsService.getOpenBills();
  }

  /**
   * Retrieves the details of the bill with the specified ID.
   *
   * @param billID the bill id.
   * @return a ResponseEntity containing the details of the bill, or a NOT_FOUND response if the
   *     bill is not found, or a INTERNAL_SERVER_ERROR response if an internal server error occurs.
   */
  @GetMapping("/bills/{billID}")
  public ResponseEntity<?> getBillDetails(@PathVariable Long billID) {
    try {
      BillDTO billDTO = consultBillsService.getBillDetails(billID);
      return ResponseEntity.ok().body(billDTO);
    } catch (BillNotFoundException e) {
      return handleException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (ApplicationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Adds the support of a citizen identified by their citizen card number to a given bill.
   *
   * @param billId the ID of the bill to support.
   * @param cc the citizen card number of the citizen who wants to support the bill.
   * @return a ResponseEntity representing the status of the operation, or a NOT_FOUND response if
   *     the bill is not found or the citizen is not found, or a INTERNAL_SERVER_ERROR response if
   *     an internal server error occurs.
   */
  @PatchMapping("/bills/support/{billId}")
  public ResponseEntity<?> supportBill(@PathVariable Long billId, @RequestBody Integer cc) {
    try {
      supportBillService.supportBill(billId, cc);
      return ResponseEntity.ok().build();
    } catch (BillNotFoundException | CitizenNotFoundException e) {
      return handleException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (CitizenAlreadySupportsBillException | VoteInClosedBillException e) {
      return handleException(HttpStatus.CONFLICT, e.getMessage());
    } catch (ApplicationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Returns a ResponseEntity with the given HTTP status and a JSON object containing a message.
   *
   * @param status the HTTP status of the response.
   * @param message the error message to be included in the response body.
   * @return a ResponseEntity with the given HTTP status and a JSON object containing a message.
   */
  private ResponseEntity<?> handleException(HttpStatus status, String message) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return ResponseEntity.status(status).headers(headers).body(Map.of("message", message));
  }
}
