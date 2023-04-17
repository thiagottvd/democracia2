package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;

/**
 * The PollCatalog class is responsible for managing polls by providing operations to retrieve, save
 * and close them. It uses a PollRepository to perform the database operations.
 */
@Component
public class PollCatalog {

  private final PollRepository pollRepository;

  /**
   * Constructs a new PollCatalog instance with the specified PollRepository.
   *
   * @param pollRepository the PollRepository to use for database access.
   */
  @Autowired
  public PollCatalog(PollRepository pollRepository) {
    this.pollRepository = pollRepository;
  }

  /**
   * Returns a list of all active polls.
   *
   * @return a List of Poll objects representing the active polls.
   */
  public List<Poll> getActivePolls() {
    return pollRepository.findAllActivePolls(PollStatus.ACTIVE);
  }

  /**
   * Saves a poll to the database.
   *
   * @param poll the Poll object to save.
   */
  public void savePoll(Poll poll) {
    pollRepository.save(poll);
  }
}
