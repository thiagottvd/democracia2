package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;

/**
 * Interface that provides data access methods for the {@link DelegateTheme} entity, using Spring
 * Data JPA.
 */
@Repository
public interface DelegateThemeRepository extends JpaRepository<DelegateTheme, Long> {

  /**
   * Retrieves a list of all DelegateThemes.
   *
   * @return a list of all DelegateThemes.
   */
  @Query("SELECT dt FROM DelegateTheme dt")
  List<DelegateTheme> getAllDTs();
}
