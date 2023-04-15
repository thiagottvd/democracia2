package pt.ul.fc.di.css.alunos.democracia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    @Query("SELECT themes FROM Theme themes")
    List<Theme> getAllThemes();
}
