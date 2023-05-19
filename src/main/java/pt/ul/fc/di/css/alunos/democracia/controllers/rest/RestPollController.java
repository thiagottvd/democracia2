package pt.ul.fc.di.css.alunos.democracia.controllers.rest;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.services.ListActivePollsService;
import pt.ul.fc.di.css.alunos.democracia.services.VoteActivePollsService;

@RestController()
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

  @GetMapping("/polls/{pollId}/public-voters/{delegateId}/vote}")
  public ResponseEntity<?> getDelegateVoteForPoll(
      @PathVariable Long pollId, @PathVariable Long delegateId) {
    // TODO
    return handleException(HttpStatus.NOT_IMPLEMENTED, "This method is not yet implemented.");
  }

  @PatchMapping("/polls/{pollId}/vote")
  public ResponseEntity<?> vote(@PathVariable Long pollId, @RequestBody Integer citizenCardNumber) {
    // TODO
    return handleException(HttpStatus.NOT_IMPLEMENTED, "This method is not yet implemented.");
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
