package pt.ul.fc.di.css.alunos.democracia.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.BillNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
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

  @Autowired
  public RestBillController(
      ConsultBillsService consultBillsService, SupportBillService supportBillService) {
    this.consultBillsService = consultBillsService;
    this.supportBillService = supportBillService;
  }

  /**
   * Retrieves a list of all open bills.
   *
   * @return a list of all open bills.
   */
  @GetMapping("/bills/open")
  List<BillDTO> getOpenBills() {
    return consultBillsService.getOpenBills();
  }

  /**
   * Retrieves the details of the bill with the specified ID.
   *
   * @param billID the bill id.
   * @return a ResponseEntity containing the details of the bill, or a BadRequest response if the
   *     bill is not found.
   * @throws RuntimeException if an ApplicationException is thrown.
   */
  @GetMapping("/bills/{billID}")
  ResponseEntity<?> getBillDetails(@PathVariable Long billID) {
    try {
      BillDTO billDTO = consultBillsService.getBillDetails(billID);
      return ResponseEntity.ok().body(billDTO);
    } catch (BillNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (ApplicationException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the support of a citizen identified by their citizen card number to a given bill.
   *
   * @param billId the ID of the bill to support.
   * @param cc the citizen card number of the citizen who wants to support the bill.
   * @return a ResponseEntity representing the status of the operation or a BadRequest response if
   *     the bill is not found or the citizen is not found.
   * @throws RuntimeException if an ApplicationException is thrown.
   */
  // @PatchMapping("/bills/support/{billId}")
  @PutMapping("/bills/support/{billId}")
  ResponseEntity<?> supportBill(@PathVariable Long billId, @RequestBody Integer cc) {
    try {
      supportBillService.supportBill(billId, cc);
      return ResponseEntity.ok().build();
    } catch (BillNotFoundException | CitizenNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (ApplicationException e) {
      throw new RuntimeException(e);
    }
  }
}
