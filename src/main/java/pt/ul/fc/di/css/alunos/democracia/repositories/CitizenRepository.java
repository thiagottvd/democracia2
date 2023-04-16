package pt.ul.fc.di.css.alunos.democracia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    @Query("SELECT c FROM Citizen c WHERE c.id = :id")
    Citizen findCitizenByID(@Param("id") long id);
}
