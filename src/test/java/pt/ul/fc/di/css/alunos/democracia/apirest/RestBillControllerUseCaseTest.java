package pt.ul.fc.di.css.alunos.democracia.apirest;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import pt.ul.fc.di.css.alunos.democracia.controllers.RestBillController;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.BillNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ConsultBillsService;
import pt.ul.fc.di.css.alunos.democracia.services.SupportBillService;

@WebMvcTest(RestBillController.class)
public class RestBillControllerUseCaseTest {

  @Autowired private MockMvc mockMvc;

  /*
   * The purpose of the billRepositoryMock is to provide a mock implementation of the BillRepository
   * for the services to use. So, even though it is not being used directly in this test class,
   * it is still being used (indirectly) by the code being tested.
   */
  @MockBean private BillRepository billRepositoryMock;

  @MockBean private ConsultBillsService consultBillsServiceMock;

  @MockBean private SupportBillService supportBillServiceMock;

  @Test
  public void testGetOpenBillsSuccessEmptyList() throws Exception {
    final int numBills = 0;

    when(consultBillsServiceMock.getOpenBills()).thenReturn(Collections.emptyList());

    mockMvc
        .perform(get("/api/bills/open").contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(numBills)));

    verify(consultBillsServiceMock, times(1)).getOpenBills();
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  @Test
  public void testGetOpenBillsSuccessWhenBillsExists() throws Exception {
    final int numOpenBills = 5;

    List<BillDTO> expectedBills = new ArrayList<>();
    for (int i = 1; i <= numOpenBills; i++) {
      expectedBills.add(new BillDTO((long) i, String.valueOf(i)));
    }

    when(consultBillsServiceMock.getOpenBills()).thenReturn(expectedBills);

    mockMvc
        .perform(get("/api/bills/open").contentType("application/json"))
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

    verify(consultBillsServiceMock, times(1)).getOpenBills();
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  @Test
  public void testGetBillDetailsSuccess() throws Exception {
    byte[] fileData = {0x10, 0x55, 0x9};

    Theme theme = new Theme("theme", null);
    Delegate delegate = new Delegate("proposer", 1);
    Bill b = new Bill("bill", "bill desc", fileData, LocalDate.now(), delegate, theme);
    Long billID = 1L;
    b.setId(billID);
    BillDTO expectedBill = new BillDTO(b);

    when(consultBillsServiceMock.getBillDetails(billID)).thenReturn(expectedBill);

    mockMvc
        .perform(get("/api/bills/" + billID).contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(expectedBill.getId()), Long.class))
        .andExpect(jsonPath("$.title", is(expectedBill.getTitle())))
        .andExpect(jsonPath("$.description", is(expectedBill.getDescription())))
        .andExpect(jsonPath("$.numSupporters", is(expectedBill.getNumSupporters())))
        // BUG: JSON serialization library used may not know how to properly serialize a byte[]
        // array to JSON.
        // .andExpect(jsonPath("$.fileData", is(expectedBill.getFileData())))
        .andExpect(
            jsonPath(
                "$.fileData", is(Base64.getEncoder().encodeToString(expectedBill.getFileData()))))
        .andExpect(jsonPath("$.expirationDate", is(expectedBill.getExpirationDate().toString())))
        .andExpect(jsonPath("$.themeDesignation", is(expectedBill.getThemeDesignation())))
        .andExpect(jsonPath("$.delegateName", is(expectedBill.getDelegateName())));

    verify(consultBillsServiceMock, times(1)).getBillDetails(billID);
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  @Test
  public void testGetBillDetailsNotFound() throws Exception {
    Long billID = 1L;

    // Set up the mock service to throw a BillNotFoundException
    when(consultBillsServiceMock.getBillDetails(billID))
        .thenThrow(new BillNotFoundException("The bill \"" + billID + "\" was not found."));

    // Perform the API call and check the response
    mockMvc.perform(get("/api/bills/" + billID)).andExpect(status().isBadRequest());

    // Verify that the service method was called
    verify(consultBillsServiceMock, times(1)).getBillDetails(billID);
    verifyNoMoreInteractions(consultBillsServiceMock);
  }

  @Test
  public void testGetBillDetailsApplicationException() throws Exception {
    Long billID = 1L;

    // Set up the mock service to throw an ApplicationException
    when(consultBillsServiceMock.getBillDetails(billID))
        .thenThrow(new ApplicationException("An error occurred while retrieving bill details."));

    // Perform the API call and check the response
    mockMvc.perform(get("/api/bills/" + billID)).andExpect(status().isInternalServerError());

    // Verify that the service method was called
    verify(consultBillsServiceMock, times(1)).getBillDetails(billID);
    verifyNoMoreInteractions(consultBillsServiceMock);
  }
}
