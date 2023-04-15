package pt.ul.fc.di.css.alunos.democracia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {}
