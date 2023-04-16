package pt.ul.fc.di.css.alunos.democracia.catalogs;

import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;

import java.util.List;

@Component
public class ThemeCatalog {
    private final ThemeRepository themeRepository;

    public ThemeCatalog(ThemeRepository themeRepository){ this.themeRepository = themeRepository; }

    /*
        Returns a list of all the themes available in the DB.
     */
    public List<Theme> getThemes(){
        return themeRepository.getAllThemes();
    }

    /*
        Returns the corresponding theme from the DB.
        @param theme The theme to retrieve.
     */
    public Theme getTheme(String designation){
        return themeRepository.findThemeByDesignation(designation);
    }
}
