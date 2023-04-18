package pt.ul.fc.di.css.alunos.democracia.catalogs;

import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;

import java.util.List;

/**
 * The ThemeCatalog class is responsible for managing themes by providing operations to retrieve, save
 * and close them. It uses a ThemeRepository to perform the database operations.
 */
@Component
public class ThemeCatalog {
    private final ThemeRepository themeRepository;

    /**
     * Constructs a new ThemeCatalog instance with the specified ThemeRepository.
     * @param themeRepository the ThemeRepository to use for database access.
     */
    public ThemeCatalog(ThemeRepository themeRepository){ this.themeRepository = themeRepository; }


    /**
     * Retrieves a list with all the available themes from the database.
     * @return The list containing all themes.
     */
    public List<Theme> getThemes(){
        return themeRepository.getAllThemes();
    }

    /**
     * Retrieves the theme with the corresponding designation from the database.
     * @param designation The theme designation we want to retrieve.
     * @return The corresponding theme if found.
     */
    public Theme getTheme(String designation){
        return themeRepository.findThemeByDesignation(designation);
    }
}
