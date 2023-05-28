package pt.ul.fc.di.css.alunos.democracia.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;

/**
 * Interface that provides data access methods for the {@link Theme} entity, using Spring Data JPA.
 */
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

  /**
   * Retrieves a list of all themes.
   *
   * @return the list of all themes.
   */
  @Query("SELECT themes FROM Theme themes")
  List<Theme> getAllThemes();

  /**
   * Retrieves a theme by its designation.
   *
   * @param designation the designation of the theme to retrieve.
   * @return an Optional containing the theme associated with the given designation, or an empty
   *     Optional if no such theme exists.
   */
  @Query("SELECT theme FROM Theme theme WHERE theme.designation = :designation")
  Optional<Theme> findThemeByDesignation(@Param("designation") String designation);
}
