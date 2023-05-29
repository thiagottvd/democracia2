package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
  @Transactional
  @Query("SELECT p FROM Poll p WHERE p.closingDate < CURRENT_DATE")
  List<Poll> findAllExpiredPolls();
}
