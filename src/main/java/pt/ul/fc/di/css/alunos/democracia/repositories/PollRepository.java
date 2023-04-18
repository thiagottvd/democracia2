package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
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

  // TODO remover essa query?
  //  /**
  //   * Updates the status of polls that have passed their closing date.
  //   *
  //   * @param statusType the new status to be set for expired polls.
  //   */
  //  @Transactional
  //  @Modifying(clearAutomatically = true)
  //  @Query("UPDATE Poll p SET p.status = :statusType WHERE p.closingDate < CURRENT_DATE")
  //  void closePolls(@Param("statusType") PollStatus statusType);

  /**
   * Retrieves a list of all expired polls.
   *
   * @return a list of all expired polls.
   */
  @Query("SELECT p FROM Poll p WHERE p.closingDate < CURRENT_DATE")
  List<Poll> findAllExpiredPolls();

  /**
   * Retrieves a poll by its associated bill title.
   *
   * @param title the title of the associated bill.
   * @return the poll associated with the given bill title.
   */
  @Query("SELECT p FROM Poll p WHERE p.associatedBill.title = :title")
  Poll findPollByTitle(@Param("title") String title);
}
