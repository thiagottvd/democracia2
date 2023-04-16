package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;

@Repository
public interface DelegateThemeRepository extends JpaRepository<DelegateTheme, Long> {

  @Query("SELECT dt FROM DelegateTheme dt")
  List<DelegateTheme> getAllDTs();
}
