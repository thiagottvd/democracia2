package pt.ul.fc.di.css.alunos.democracia.api_rest;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.di.css.alunos.democracia.controllers.RestBillController;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
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
  public void testGetOpenBillsReturn200OKEmptyList() throws Exception {
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
  public void testGetOpenBillsReturn200OKWhenBillsExists() throws Exception {
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
}
