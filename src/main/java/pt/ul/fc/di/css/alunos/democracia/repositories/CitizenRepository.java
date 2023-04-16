package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
  @Query("SELECT c FROM Citizen c WHERE c.nif = :nif")
  Citizen findCitizenByNif(@Param("nif") int nif);

  @Query("SELECT d FROM Delegate d")
  List<Delegate> getAllDelegates();

  @Query("SELECT d FROM Delegate d WHERE d.nif = :nif")
  Delegate findDelegateByNif(@Param("nif") int nif);
}
