package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.handlers.ListActivePollsHandler;

/**
 * Use case D.
 *
 * <p>Service class that retrieves a list of the current polls being voted calling the appropriate
 * handler {@link ListActivePollsHandler} to retrieve the active polls and converting them into a
 * list of PollDTO objects.
 */
@Service
public class ListActivePollsService {

  private final ListActivePollsHandler listActivePollsHandler;

  /**
   * Constructor for the ListActivePollsService class. It takes a ListActivePollsHandler object as
   * parameter and sets it as an attribute.
   *
   * @param listActivePollsHandler the handler responsible for retrieving the current active polls.
   */
  @Autowired
  public ListActivePollsService(ListActivePollsHandler listActivePollsHandler) {
    this.listActivePollsHandler = listActivePollsHandler;
  }

  /**
   * Returns a list of active polls in the form of PollDTO objects.
   *
   * @return a list of active polls.
   */
  public List<PollDTO> getActivePolls() {
    return listActivePollsHandler.getActivePolls();
  }
}
