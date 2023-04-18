package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;

/**
 * Interface that provides data access methods for the {@link Citizen} entity, using Spring Data
 * JPA.
 */
@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

  /**
   * Finds a Citizen entity by their citizen card number (CC).
   *
   * @param cc the citizen card number of the Citizen entity to retrieve.
   * @return an Optional containing the Citizen entity with the specified CC, or empty if not found.
   */
  @Query("SELECT c FROM Citizen c WHERE c.cc = :cc")
  Optional<Citizen> findByCc(@Param("cc") int cc);

  /**
   * Retrieves a list of all Delegate entities.
   *
   * @return a list of all Delegate entities.
   */
  @Query("SELECT d FROM Delegate d")
  List<Delegate> getAllDelegates();

  /**
   * Finds a Delegate entity by their citizen card number (CC).
   *
   * @param cc the citizen card number (CC) of the Delegate entity to retrieve.
   * @return the Delegate entity with the specified CC, or null if not found.
   */
  @Query("SELECT d FROM Delegate d WHERE d.cc = :cc")
  Delegate findDelegateByCc(@Param("cc") int cc);

  /**
   * Retrieves a list of all Citizen entities that did not vote in a poll.
   *
   * @param voters a list of Citizen entities that voted in a poll.
   * @return a list of all Citizen entities that did not vote in a poll.
   */
  @Query("SELECT nv FROM Citizen nv WHERE nv NOT IN (SELECT v FROM Citizen v WHERE v IN :voters)")
  List<Citizen> findAllNonVoters(@Param("voters") List<Citizen> voters);
}
