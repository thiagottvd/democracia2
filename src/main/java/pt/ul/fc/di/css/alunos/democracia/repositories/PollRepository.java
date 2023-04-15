package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;

public interface PollRepository extends JpaRepository<Poll, Long> {

  @Query("SELECT p FROM Poll p WHERE p.status = :status")
  List<Poll> findAllActivePolls(@Param("status") PollStatus status);
}
