package pt.ul.fc.di.css.alunos.democracia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE Bill b SET b.status = :status WHERE b.expirationDate < CURRENT_DATE")
  void closeExpiredBills(@Param("status") BillStatus status);
}
