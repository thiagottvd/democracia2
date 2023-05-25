package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;

/**
 * Handles use case D.
 *
 * <p>Handler that retrieves a list of the current active polls and converts them to a list of
 * {@link PollDTO} objects.
 */
@Component
public class ListActivePollsHandler {

  private final PollCatalog pollCatalog;

  /**
   * Constructor for the ListActivePollsHandler class. It takes a PollCatalog object as parameter
   * and sets it as an attribute.
   *
   * @param pollCatalog the catalog responsible for managing polls.
   */
  @Autowired
  public ListActivePollsHandler(PollCatalog pollCatalog) {
    this.pollCatalog = pollCatalog;
  }

  /**
   * Returns a list of active polls in the form of PollDTO objects.
   *
   * @return a list of active polls.
   */
  public List<PollDTO> getActivePolls() {
    List<Poll> activePolls = pollCatalog.getPollsByStatusType(PollStatus.ACTIVE);
    return activePolls.stream()
        .map(poll -> new PollDTO(poll.getId(), poll.getAssociatedBill().getTitle()))
        .collect(Collectors.toList());
  }
}
