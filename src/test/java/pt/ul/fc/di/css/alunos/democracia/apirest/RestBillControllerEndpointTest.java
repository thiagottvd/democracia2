package pt.ul.fc.di.css.alunos.democracia.apirest;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pt.ul.fc.di.css.alunos.democracia.controllers.rest.RestBillController;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.*;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ConsultBillsService;
import pt.ul.fc.di.css.alunos.democracia.services.SupportBillService;

/**
 * This class tests the use cases of the {@link RestBillController} class, which deals with REST
 * calls related to the management of bills.
 */
@WebMvcTest(RestBillController.class)
public class RestBillControllerEndpointTest {

  @Autowired private MockMvc mockMvc;

  /**
   * Provides a mock implementation of the {@link BillRepository} for the services to use. It is not
   * used directly in this test class but indirectly by the code being tested.
   */
  @MockBean private BillRepository billRepositoryMock;

  @MockBean private ConsultBillsService consultBillsServiceMock;

  @MockBean private SupportBillService supportBillServiceMock;

  private static final String CONTENT_TYPE = "application/json";
  private static final String GET_OPEN_BILLS_ENDPOINT_URL = "/api/bills/open";
  private static final String GET_BILL_DETAILS_ENDPOINT_URL = "/api/bills/";

  /**
   * Tests the REST endpoint, getOpenBills, that retrieves a list of all open bills. This test case
   * considers an empty list.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testGetOpenBillsSuccessEmptyList() throws Exception {
    // Set up test data
    final int numBills = 0;

    // Set up the mock service to return an empty list.
    when(consultBillsServiceMock.getOpenBills()).thenReturn(Collections.emptyList());

    // Perform the API call and check the response
    mockMvc
        .perform(get(GET_OPEN_BILLS_ENDPOINT_URL).contentType(CONTENT_TYPE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(numBills)));

    // Verify that the service method was called
    verify(consultBillsServiceMock, times(1)).getOpenBills();
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  /**
   * Tests the REST endpoint, getOpenBills, that retrieves a list of all open bills. This test case
   * considers a non-empty list.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testGetOpenBillsSuccessWhenBillsExists() throws Exception {
    // Set up test data
    final int numOpenBills = 5;

    List<BillDTO> expectedBills = new ArrayList<>();
    for (int i = 1; i <= numOpenBills; i++) {
      expectedBills.add(new BillDTO((long) i, String.valueOf(i)));
    }

    // Set up the mock service to return the expected open bills list.
    when(consultBillsServiceMock.getOpenBills()).thenReturn(expectedBills);

    // Perform the API call and check the response
    mockMvc
        .perform(get(GET_OPEN_BILLS_ENDPOINT_URL).contentType(CONTENT_TYPE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(numOpenBills)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 3, 4, 5)))
        .andExpect(jsonPath("$[*].title", containsInAnyOrder("1", "2", "3", "4", "5")))
        .andExpect(jsonPath("$[*].description", everyItem(nullValue())))
        .andExpect(jsonPath("$[*].numSupporters", everyItem(is(0))))
        .andExpect(jsonPath("$[*].fileData", everyItem(nullValue())))
        .andExpect(jsonPath("$[*].expirationDate", everyItem(nullValue())))
        .andExpect(jsonPath("$[*].themeDesignation", everyItem(nullValue())))
        .andExpect(jsonPath("$[*].delegateName", everyItem(nullValue())));

    // Verify that the service method was called
    verify(consultBillsServiceMock, times(1)).getOpenBills();
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  /**
   * Tests the REST endpoint, getBillDetails, that retrieves the details of the bill with the
   * specified ID. This test case consider a successful case scenario.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testGetBillDetailsSuccess() throws Exception {
    // Set up test data
    byte[] fileData = {0x10, 0x55, 0x9};

    Theme theme = new Theme("theme", null);
    Delegate delegate = new Delegate("proposer", 1);
    Bill b = new Bill("bill", "bill desc", fileData, LocalDate.now(), delegate, theme);
    Long billID = 1L;
    b.setId(billID);
    BillDTO expectedBill = new BillDTO(b);

    // Set up the mock service to return the expected bill
    when(consultBillsServiceMock.getBillDetails(billID)).thenReturn(expectedBill);

    // Perform the API call and check the response
    mockMvc
        .perform(get(GET_BILL_DETAILS_ENDPOINT_URL + billID).contentType(CONTENT_TYPE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(expectedBill.getId()), Long.class))
        .andExpect(jsonPath("$.title", is(expectedBill.getTitle())))
        .andExpect(jsonPath("$.description", is(expectedBill.getDescription())))
        .andExpect(jsonPath("$.numSupporters", is(expectedBill.getNumSupporters())))
        .andExpect(
            jsonPath(
                "$.fileData", is(Base64.getEncoder().encodeToString(expectedBill.getFileData()))))
        .andExpect(jsonPath("$.expirationDate", is(expectedBill.getExpirationDate().toString())))
        .andExpect(jsonPath("$.themeDesignation", is(expectedBill.getThemeDesignation())))
        .andExpect(jsonPath("$.delegateName", is(expectedBill.getDelegateName())));

    // Verify that the service method was called
    verify(consultBillsServiceMock, times(1)).getBillDetails(billID);
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  /**
   * Tests the REST endpoint, getBillDetails, that retrieves the details of the bill with the
   * specified ID. It tests when the bill information is successfully retrieved.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testGetBillDetailsNotFound() throws Exception {
    // Set up test data
    Long billID = 1L;

    // Set up the mock service to throw a BillNotFoundException
    when(consultBillsServiceMock.getBillDetails(billID))
        .thenThrow(new BillNotFoundException("The bill \"" + billID + "\" was not found."));

    // Perform the API call and check the response
    mockMvc.perform(get(GET_BILL_DETAILS_ENDPOINT_URL + billID)).andExpect(status().isNotFound());

    // Verify that the service method was called
    verify(consultBillsServiceMock, times(1)).getBillDetails(billID);
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  /**
   * Tests the REST endpoint, getBillDetails, that retrieves the details of the bill with the
   * specified ID. It tests when the bill is not found.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testGetBillDetailsBillNotFound() throws Exception {
    // Set up test data
    Long billID = 1L;

    // Set up the mock service to throw a BillNotFoundException
    when(consultBillsServiceMock.getBillDetails(billID))
        .thenThrow(new BillNotFoundException("The bill \"" + billID + "\" was not found."));

    // Perform the API call and check the response
    mockMvc
        .perform(get(GET_BILL_DETAILS_ENDPOINT_URL + billID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("The bill \"" + billID + "\" was not found."));

    // Verify that the service method was called
    verify(consultBillsServiceMock, times(1)).getBillDetails(billID);
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  /**
   * Tests the REST endpoint, getBillDetails, that retrieves the details of the bill with the
   * specified ID. It tests when an error occurred while retrieving bill details.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testGetBillDetailsApplicationException() throws Exception {
    // Set up test data
    Long billID = 1L;

    // Set up the mock service to throw an ApplicationException
    when(consultBillsServiceMock.getBillDetails(billID))
        .thenThrow(new ApplicationException("An error occurred while retrieving bill details."));

    // Perform the API call and check the response
    mockMvc
        .perform(get(GET_BILL_DETAILS_ENDPOINT_URL + billID))
        .andExpect(status().isInternalServerError());

    // Verify that the service method was called
    verify(consultBillsServiceMock, times(1)).getBillDetails(billID);
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  /**
   * Tests the REST endpoint, supportBill, that adds the support of a citizen identified by their
   * citizen card number to a given bill. It tests when the bill support successes.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testSupportBillSuccess() throws Exception {
    // Set up test data
    Long billId = 1L;
    Integer cc = 123456789;

    // Set up the mock service to not throw any exceptions
    doNothing().when(supportBillServiceMock).supportBill(billId, cc);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/bills/" + billId + "/support").contentType(CONTENT_TYPE).content(cc.toString());

    // Perform the API call and check the response
    mockMvc.perform(request).andExpect(status().isOk());

    // Verify that the service method was called
    verify(supportBillServiceMock, times(1)).supportBill(billId, cc);
    verifyNoMoreInteractions(supportBillServiceMock);
  }

  /**
   * Tests the REST endpoint, supportBill, that adds the support of a citizen identified by their
   * citizen card number to a given bill. It tests when the bill is not found.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testSupportBillWhenBillNotFound() throws Exception {
    // Set up test data
    Long billId = 1L;
    Integer cc = 123456789;

    // Set up the mock service to throw a BillNotFoundException
    doThrow(new BillNotFoundException("The bill with id " + billId + " was not found."))
        .when(supportBillServiceMock)
        .supportBill(billId, cc);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/bills/" + billId + "/support").contentType(CONTENT_TYPE).content(cc.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("The bill with id " + billId + " was not found."));

    // Verify that the service method was called
    verify(supportBillServiceMock, times(1)).supportBill(billId, cc);
    verifyNoMoreInteractions(supportBillServiceMock);
  }

  /**
   * Tests the REST endpoint, supportBill, that adds the support of a citizen identified by their
   * citizen card number to a given bill. It tests when the citizen is not found.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testSupportBillCitizenNotFound() throws Exception {
    // Set up test data
    Long billId = 1L;
    Integer cc = 123456789;

    // Set up the mock service to throw a CitizenNotFoundException
    doThrow(
            new CitizenNotFoundException(
                "The citizen with citizen card number " + cc + " was not found."))
        .when(supportBillServiceMock)
        .supportBill(billId, cc);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/bills/" + billId + "/support").contentType(CONTENT_TYPE).content(cc.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.message")
                .value("The citizen with citizen card number " + cc + " was not found."));

    // Verify that the service method was called
    verify(supportBillServiceMock, times(1)).supportBill(billId, cc);
    verifyNoMoreInteractions(supportBillServiceMock);
  }

  /**
   * Tests the REST endpoint, supportBill, that adds the support of a citizen identified by their
   * citizen card number to a given bill. It tests when an error occurred while supporting a bill.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testSupportBillApplicationException() throws Exception {
    // Set up test data
    Long billId = 1L;
    Integer cc = 123456789;

    // Set up the mock service to throw an ApplicationException
    doThrow(new ApplicationException("An error occurred while supporting the bill."))
        .when(supportBillServiceMock)
        .supportBill(billId, cc);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/bills/" + billId + "/support").contentType(CONTENT_TYPE).content(cc.toString());

    // Perform the API call and check the response
    mockMvc.perform(request).andExpect(status().isInternalServerError());

    // Verify that the service method was called
    verify(supportBillServiceMock, times(1)).supportBill(billId, cc);
    verifyNoMoreInteractions(supportBillServiceMock);
  }

  /**
   * Tests the REST endpoint, supportBill, that adds the support of a citizen identified by their
   * citizen card number to a given bill. It tests when a citizen tries to support a bill more than
   * one time.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testSupportBillTwice() throws Exception {
    // Set up test data
    Long billId = 1L;
    Integer cc = 123456789;

    // Set up the mock service to throw a CitizenAlreadySupportsBillException
    doThrow(
            new CitizenAlreadySupportsBillException(
                "The citizen with cc " + cc + " already supports bill with id " + billId + "."))
        .when(supportBillServiceMock)
        .supportBill(billId, cc);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/bills/" + billId + "/support").contentType(CONTENT_TYPE).content(cc.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isConflict())
        .andExpect(
            jsonPath("$.message")
                .value(
                    "The citizen with cc "
                        + cc
                        + " already supports bill with id "
                        + billId
                        + "."));

    // Verify that the service method was called
    verify(supportBillServiceMock, times(1)).supportBill(billId, cc);
    verifyNoMoreInteractions(supportBillServiceMock);
  }

  /**
   * Tests the REST endpoint, supportBill, that adds the support of a citizen identified by their
   * citizen card number to a given bill. It tests when a citizen tries to support a bill that is
   * closed.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testSupportBillWhenClosed() throws Exception {
    // Set up test data
    Long billId = 1L;
    Integer cc = 123456789;

    // Set up the mock service to throw an VoteInClosedBillException
    doThrow(
            new VoteInClosedBillException(
                "The citizen with cc "
                    + cc
                    + " can not vote for bill with id "
                    + billId
                    + " because it is closed."))
        .when(supportBillServiceMock)
        .supportBill(billId, cc);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/bills/" + billId + "/support").contentType(CONTENT_TYPE).content(cc.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isConflict())
        .andExpect(
            jsonPath("$.message")
                .value(
                    "The citizen with cc "
                        + cc
                        + " can not vote for bill with id "
                        + billId
                        + " because it is closed."));

    // Verify that the service method was called
    verify(supportBillServiceMock, times(1)).supportBill(billId, cc);
    verifyNoMoreInteractions(supportBillServiceMock);
  }
}
