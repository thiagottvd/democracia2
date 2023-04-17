package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
  @Query("SELECT c FROM Citizen c WHERE c.cc = :cc")
  Optional<Citizen> findByCc(@Param("cc") int cc);

  @Query("SELECT d FROM Delegate d")
  List<Delegate> getAllDelegates();

  @Query("SELECT d FROM Delegate d WHERE d.cc = :cc")
  Delegate findDelegateByCc(@Param("cc") int cc);

  @Query("SELECT nv FROM Citizen nv WHERE nv NOT IN (SELECT v FROM Citizen v WHERE v IN :voters)")
  List<Citizen> findAllNonVoters(@Param("voters") List<Citizen> voters);
}
