package pt.ul.fc.di.css.alunos.democracia.apirest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
import pt.ul.fc.di.css.alunos.democracia.controllers.rest.RestPollController;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ListActivePollsService;
import pt.ul.fc.di.css.alunos.democracia.services.VoteActivePollsService;

/**
 * This class tests the use cases of the {@link RestPollController} class, which deals with REST
 * calls related to the management of polls.
 */
@WebMvcTest(RestPollController.class)
public class RestPollControllerEndpointTest {

  @Autowired private MockMvc mockMvc;

  /**
   * Provides a mock implementation of the {@link PollRepository} for the services to use. It is not
   * used directly in this test class but indirectly by the code being tested.
   */
  @MockBean private PollRepository pollRepositoryMock;

  @MockBean private ListActivePollsService listActivePollsServiceMock;

  @MockBean private VoteActivePollsService voteActivePollsServiceMock;

  private static final String CONTENT_TYPE = "application/json";

  private static final String GET_ACTIVE_POLLS_ENDPOINT_URL = "/api/polls/active";

  /**
   * Tests the REST endpoint, getActivePolls, that retrieves a list of all active polls. This test
   * case considers an empty list.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testGetActivePollsSuccessEmptyList() throws Exception {
    // Set up test data
    final int numPolls = 0;

    // Set up the mock service to return an empty list.
    when(listActivePollsServiceMock.getActivePolls()).thenReturn(Collections.emptyList());

    // Perform the API call and check the response
    mockMvc
        .perform(get(GET_ACTIVE_POLLS_ENDPOINT_URL).contentType(CONTENT_TYPE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(numPolls)));

    // Verify that the service method was called
    verify(listActivePollsServiceMock, times(1)).getActivePolls();
    verifyNoMoreInteractions(listActivePollsServiceMock);
  }

  /**
   * Tests the REST endpoint, getActivePolls, that retrieves a list of all active polls. This test
   * case considers a non-empty list.
   *
   * @throws Exception if any exception occurs during the HTTP request.
   */
  @Test
  public void testGetActivePollsSuccessWhenPollsExists() throws Exception {
    // Set up test data
    final int numActivePolls = 5;

    List<PollDTO> expectedPolls = new ArrayList<>();
    for (int i = 1; i <= numActivePolls; i++) {
      expectedPolls.add(new PollDTO((long) i, String.valueOf(i)));
    }

    // Set up the mock service to return the expected active polls list.
    when(listActivePollsServiceMock.getActivePolls()).thenReturn(expectedPolls);

    // Perform the API call and check the response
    mockMvc
        .perform(get(GET_ACTIVE_POLLS_ENDPOINT_URL).contentType(CONTENT_TYPE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(numActivePolls)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 3, 4, 5)))
        .andExpect(jsonPath("$[*].title", containsInAnyOrder("1", "2", "3", "4", "5")));

    // Verify that the service method was called
    verify(listActivePollsServiceMock, times(1)).getActivePolls();
    verifyNoMoreInteractions(listActivePollsServiceMock);
  }
}
