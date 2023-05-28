package pt.ul.fc.di.css.alunos.democracia.controllers.rest;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.*;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.services.ListActivePollsService;
import pt.ul.fc.di.css.alunos.democracia.services.VoteActivePollsService;

@RestController
@RequestMapping("api")
public class RestPollController {

  private final ListActivePollsService listActivePollsService;
  private final VoteActivePollsService voteActivePollsService;

  @Autowired
  public RestPollController(
      ListActivePollsService listActivePollsService,
      VoteActivePollsService voteActivePollsService) {
    this.listActivePollsService = listActivePollsService;
    this.voteActivePollsService = voteActivePollsService;
  }

  /**
   * Retrieves a list of all active polls.
   *
   * @return a list of all active polls.
   */
  @GetMapping("/polls/active")
  public List<PollDTO> getActivePolls() {
    return listActivePollsService.getActivePolls();
  }

  @GetMapping("/polls/{pollId}/delegate-vote-type")
  public ResponseEntity<?> getDelegateVoteForPoll(
      @PathVariable Long pollId, @RequestBody Integer citizenCardNumber) {
    try {
      VoteType voteType = this.voteActivePollsService.checkDelegateVote(pollId, citizenCardNumber);
      return ResponseEntity.ok().body(voteType);
    } catch (PollNotFoundException | CitizenNotFoundException e) {
      return handleException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (ApplicationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping("/polls/{pollId}/vote/{voteType}")
  public ResponseEntity<?> vote(
      @PathVariable Long pollId,
      @PathVariable VoteType voteType,
      @RequestBody Integer citizenCardNumber) {
    try {
      this.voteActivePollsService.vote(pollId, citizenCardNumber, voteType);
      return ResponseEntity.ok().build();
    } catch (PollNotFoundException | CitizenNotFoundException e) {
      return handleException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (InvalidVoteTypeException | CitizenAlreadyVotedException e) {
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
