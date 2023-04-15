package pt.ul.fc.di.css.alunos.democracia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;

public interface BillRepository extends JpaRepository<Poll, Long> {}
