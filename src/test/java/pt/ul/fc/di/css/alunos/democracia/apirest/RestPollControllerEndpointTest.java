package pt.ul.fc.di.css.alunos.democracia.apirest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pt.ul.fc.di.css.alunos.democracia.controllers.rest.RestPollController;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.*;
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

  @Test
  public void testGetDelegateVoteForPollSuccessWhenNoVote() throws Exception {
    // Set up test data
    Long pollId = 1L;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to return the expected delegate vote (null, in this case).
    when(voteActivePollsServiceMock.checkDelegateVote(pollId, voterCitizenCardNumber))
        .thenReturn(null);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        get("/api/polls/" + pollId + "/delegate-vote-type")
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response (should be an empty string, because the delegate
    // did not vote)
    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(""));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).checkDelegateVote(pollId, voterCitizenCardNumber);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the behavior of the 'getDelegateVoteForPoll' API endpoint when the delegate
   * has not voted yet.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testGetDelegateVoteForPollSuccessWhenPositiveVote() throws Exception {
    // Set up test data
    Long pollId = 1L;
    Integer voterCitizenCardNumber = 1;
    VoteType positiveVote = VoteType.POSITIVE;
    String expectedContent = "\"POSITIVE\"";

    // Set up the mock service to return the expected delegate vote (VoteType.POSITIVE, in this
    // case).
    when(voteActivePollsServiceMock.checkDelegateVote(pollId, voterCitizenCardNumber))
        .thenReturn(positiveVote);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        get("/api/polls/" + pollId + "/delegate-vote-type")
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response (should be a string with value POSITIVE, because
    // the delegate voted VoteType.POSITIVE)
    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(content().string(expectedContent));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).checkDelegateVote(pollId, voterCitizenCardNumber);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the behavior of the 'getDelegateVoteForPoll' API endpoint when the delegate
   * has voted negatively.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testGetDelegateVoteForPollSuccessWhenNegativeVote() throws Exception {
    // Set up test data
    Long pollId = 1L;
    Integer voterCitizenCardNumber = 1;
    VoteType negativeVote = VoteType.NEGATIVE;
    String expectedContent = "\"NEGATIVE\"";

    // Set up the mock service to return the expected delegate vote (VoteType.NEGATIVE, in this
    // case).
    when(voteActivePollsServiceMock.checkDelegateVote(pollId, voterCitizenCardNumber))
        .thenReturn(negativeVote);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        get("/api/polls/" + pollId + "/delegate-vote-type")
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response (should be a string with value NEGATIVE, because
    // the delegate voted VoteType.NEGATIVE)
    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(content().string(expectedContent));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).checkDelegateVote(pollId, voterCitizenCardNumber);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the behavior of the 'getDelegateVoteForPoll' API endpoint when the poll
   * doesnt exist.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testGetDelegateVoteForPollPollNotFoundException() throws Exception {
    // Set up test data
    Long pollId = 1L;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to throw a PollNotFoundException
    when(voteActivePollsServiceMock.checkDelegateVote(pollId, voterCitizenCardNumber))
        .thenThrow(new PollNotFoundException("Poll with id: " + pollId + " was not found."));

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        get("/api/polls/" + pollId + "/delegate-vote-type")
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Poll with id: " + pollId + " was not found."));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).checkDelegateVote(pollId, voterCitizenCardNumber);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the behavior of the 'getDelegateVoteForPoll' API endpoint when the citizen
   * does not exist.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testGetDelegateVoteForPollCitizenNotFoundException() throws Exception {
    // Set up test data
    Long pollId = 1L;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to throw a CitizenNotFoundException
    when(voteActivePollsServiceMock.checkDelegateVote(pollId, voterCitizenCardNumber))
        .thenThrow(
            new CitizenNotFoundException(
                "Citizen with id: " + voterCitizenCardNumber + " not found."));

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        get("/api/polls/" + pollId + "/delegate-vote-type")
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.message")
                .value("Citizen with id: " + voterCitizenCardNumber + " not found."));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).checkDelegateVote(pollId, voterCitizenCardNumber);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the behavior of the 'getDelegateVoteForPoll' in an application exception.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testGetDelegateVoteForPollApplicationException() throws Exception {
    // Set up test data
    Long pollId = 1L;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to throw an ApplicationException
    when(voteActivePollsServiceMock.checkDelegateVote(pollId, voterCitizenCardNumber))
        .thenThrow(
            new ApplicationException("An error occurred while retrieving the delegate vote."));

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        get("/api/polls/" + pollId + "/delegate-vote-type")
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc.perform(request).andExpect(status().isInternalServerError());

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).checkDelegateVote(pollId, voterCitizenCardNumber);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the successful behavior of the 'vote' API endpoint when a positive vote is
   * submitted.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testVoteSuccessWhenPositiveVote() throws Exception {
    // Set up test data
    Long pollId = 1L;
    VoteType voteType = VoteType.POSITIVE;
    Integer voterCitizenCardNumber = 1;

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/polls/" + pollId + "/vote/" + voteType)
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc.perform(request).andExpect(status().isOk());

    verify(voteActivePollsServiceMock, times(1)).vote(pollId, voterCitizenCardNumber, voteType);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the successful behavior of the 'vote' API endpoint when a negative vote is
   * submitted.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testVoteSuccessWhenNegativeVote() throws Exception {
    // Set up test data
    Long pollId = 1L;
    VoteType voteType = VoteType.NEGATIVE;
    Integer voterCitizenCardNumber = 1;

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/polls/" + pollId + "/vote/" + voteType)
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc.perform(request).andExpect(status().isOk());

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).vote(pollId, voterCitizenCardNumber, voteType);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the successful behavior of the 'vote' API endpoint when the poll does not
   * exist.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testVotePollNotFoundException() throws Exception {
    // Set up test data
    Long pollId = 1L;
    VoteType voteType = VoteType.NEGATIVE;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to throw a PollNotFoundException
    doThrow(new PollNotFoundException("Poll with id: " + pollId + " was not found."))
        .when(voteActivePollsServiceMock)
        .vote(pollId, voterCitizenCardNumber, voteType);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/polls/" + pollId + "/vote/" + voteType)
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Poll with id: " + pollId + " was not found."));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).vote(pollId, voterCitizenCardNumber, voteType);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the successful behavior of the 'vote' API endpoint when the citizen does
   * not exist.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testVoteCitizenNotFoundException() throws Exception {
    // Set up test data
    Long pollId = 1L;
    VoteType voteType = VoteType.POSITIVE;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to throw a CitizenNotFoundException
    doThrow(
            new CitizenNotFoundException(
                "Citizen with id: " + voterCitizenCardNumber + " not found."))
        .when(voteActivePollsServiceMock)
        .vote(pollId, voterCitizenCardNumber, voteType);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/polls/" + pollId + "/vote/" + voteType)
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.message")
                .value("Citizen with id: " + voterCitizenCardNumber + " not found."));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).vote(pollId, voterCitizenCardNumber, voteType);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the successful behavior of the 'vote' API endpoint when the VoteType is
   * invalid.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testVoteInvalidVoteTypeException() throws Exception {
    // Set up test data
    Long pollId = 1L;
    VoteType voteType = VoteType.POSITIVE;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to throw an InvalidVoteTypeException
    doThrow(new InvalidVoteTypeException("The vote type is invalid."))
        .when(voteActivePollsServiceMock)
        .vote(pollId, voterCitizenCardNumber, voteType);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/polls/" + pollId + "/vote/" + voteType)
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("The vote type is invalid."));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).vote(pollId, voterCitizenCardNumber, voteType);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the successful behavior of the 'vote' API endpoint when the citizen already
   * voted in the poll.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testVoteCitizenAlreadyVotedExceptionException() throws Exception {
    // Set up test data
    Long pollId = 1L;
    VoteType voteType = VoteType.POSITIVE;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to throw a CitizenAlreadyVoted
    doThrow(
            new CitizenAlreadyVotedException(
                "Citizen with citizen card number "
                    + voterCitizenCardNumber
                    + " has already voted in this poll."))
        .when(voteActivePollsServiceMock)
        .vote(pollId, voterCitizenCardNumber, voteType);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/polls/" + pollId + "/vote/" + voteType)
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc
        .perform(request)
        .andExpect(status().isConflict())
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Citizen with citizen card number "
                        + voterCitizenCardNumber
                        + " has already voted in this poll."));

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).vote(pollId, voterCitizenCardNumber, voteType);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }

  /**
   * Test case to verify the successful behavior of the 'vote' API endpoint in an application
   * exception..
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testVoteApplicationException() throws Exception {
    // Set up test data
    Long pollId = 1L;
    VoteType voteType = VoteType.POSITIVE;
    Integer voterCitizenCardNumber = 1;

    // Set up the mock service to throw an ApplicationException
    doThrow(new ApplicationException(("An error occurred while voting.")))
        .when(voteActivePollsServiceMock)
        .vote(pollId, voterCitizenCardNumber, voteType);

    // Set up the request with the request body
    MockHttpServletRequestBuilder request =
        patch("/api/polls/" + pollId + "/vote/" + voteType)
            .contentType(CONTENT_TYPE)
            .content(voterCitizenCardNumber.toString());

    // Perform the API call and check the response
    mockMvc.perform(request).andExpect(status().isInternalServerError());

    // Verify that the service method was called
    verify(voteActivePollsServiceMock, times(1)).vote(pollId, voterCitizenCardNumber, voteType);
    verifyNoMoreInteractions(voteActivePollsServiceMock);
  }
}
