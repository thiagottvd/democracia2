package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

  @Query("SELECT p FROM Poll p WHERE p.status = :statusType")
  List<Poll> findAllPolls(@Param("statusType") PollStatus statusType);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE Poll p SET p.status = :statusType WHERE p.closingDate < CURRENT_DATE")
  void closePolls(@Param("statusType") PollStatus statusType);

  @Query("SELECT p FROM Poll p WHERE p.closingDate < CURRENT_DATE")
  List<Poll> findAllExpiredPolls();

  @Query("SELECT p FROM Poll p WHERE p.associatedBill.title = :title")
  Poll findPollByTitle(@Param("title") String title);
}
