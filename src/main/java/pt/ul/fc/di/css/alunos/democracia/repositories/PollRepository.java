package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;

/**
 * Interface that provides data access methods for the {@link Poll} entity, using Spring Data JPA.
 */
@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

  /**
   * Retrieves a list of all polls with the given status.
   *
   * @param statusType the status of the polls to be retrieved.
   * @return a list of all polls with the given status.
   */
  @Query("SELECT p FROM Poll p WHERE p.status = :statusType")
  List<Poll> findAllPolls(@Param("statusType") PollStatus statusType);

  /**
   * Retrieves a list of all expired polls.
   *
   * @return a list of all expired polls.
   */
  @Query("SELECT p FROM Poll p WHERE p.closingDate < CURRENT_DATE")
  List<Poll> findAllExpiredPolls();

  /**
   * Retrieves the poll associated with a bill of the given title.
   *
   * @param title the title of the bill to retrieve the associated poll for.
   * @return an Optional containing the poll associated with the given bill title, or an empty
   *     Optional if no such poll exists.
   */
  @Query("SELECT p FROM Poll p WHERE p.associatedBill.title = :title")
  Optional<Poll> findPollByTitle(@Param("title") String title);
}
