package pt.ul.fc.di.css.alunos.democracia.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProposeBillHandler {

    private final ThemeCatalog themeCatalog;

    @Autowired
    public ProposeBillHandler(ThemeCatalog themeCatalog) {
        this.themeCatalog = themeCatalog;
    }

    public List<ThemeDTO> getThemes(){
        List<Theme> themes = themeCatalog.getThemes();
        return themes.stream()
                .map(theme -> new ThemeDTO(theme.getDesignation()))
                .collect(Collectors.toList());
    }

    public void proposeBill(String title, String description, File pdf, LocalDate expirationDate, Theme theme, Delegate delegate){

    }
}
