package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
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
   * Retrieves a list of polls with the specified status type.
   *
   * @param statusType the status type of the polls to retrieve.
   * @return a list of polls with the specified status type.
   */
  public List<Poll> getPollsByStatusType(PollStatus statusType) {
    return pollRepository.findAllPolls(statusType);
  }

  /**
   * Returns the Poll with a given title.
   *
   * @param title The title of the Poll we need.
   * @return The corresponding Poll.
   */
  public Poll getPollByTitle(String title) {
    return pollRepository.findPollByTitle(title);
  }

  /**
   * Saves a poll to the database.
   *
   * @param poll the Poll object to save.
   */
  public void savePoll(Poll poll) {
    pollRepository.save(poll);
  }

  public List<Poll> getExpiredPolls() {
    return pollRepository.findAllExpiredPolls();
  }
}
