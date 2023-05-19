package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.di.css.alunos.democracia.datatypes.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

/**
 * Interface that provides data access methods for the {@link Bill} entity, using Spring Data JPA.
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

  /**
   * Retrieves a list of all open bills with the given status.
   *
   * @param status the status of the bills to be retrieved (should always be BillStatus.OPEN).
   * @return a list of all open bills with the given status.
   */
  @Query("SELECT b FROM Bill b WHERE b.status = :status")
  List<Bill> findAllOpenBills(@Param("status") BillStatus status);

  /**
   * Closes all bills that have expired and sets their status to the given status.
   *
   * @param status the status to set for the expired bills (should always be BillStatus.CLOSED).
   */
  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE Bill b SET b.status = :status WHERE b.expirationDate < CURRENT_DATE")
  void closeExpiredBills(@Param("status") BillStatus status);
}
