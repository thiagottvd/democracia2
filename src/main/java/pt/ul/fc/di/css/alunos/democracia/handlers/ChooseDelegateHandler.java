package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.DelegateThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.DelegateDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;

public class ChooseDelegateHandler {

  private final ThemeCatalog themeCatalog;
  private final DelegateThemeCatalog dtCatalog;
  private final CitizenCatalog citizenCatalog;

  @Autowired
  public ChooseDelegateHandler(
      ThemeCatalog themeCatalog, DelegateThemeCatalog dtCatalog, CitizenCatalog citizenCatalog) {
    this.themeCatalog = themeCatalog;
    this.dtCatalog = dtCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  public List<DelegateDTO> getDelegates() {
    // TODO
    List<Delegate> delegates = citizenCatalog.getDelegates();
    return delegates.stream()
        .map(delegate -> new DelegateDTO(delegate.getName(), delegate.getNif()))
        .collect(Collectors.toList());
  }

  public List<ThemeDTO> getThemes() {
    List<Theme> themes = themeCatalog.getThemes();
    return themes.stream()
        .map(theme -> new ThemeDTO(theme.getDesignation()))
        .collect(Collectors.toList());
  }

  public void chooseDelegate(DelegateDTO delegate, ThemeDTO theme, int nif) {

    Delegate d = citizenCatalog.getDelegate(delegate.getNif());
    Theme t = themeCatalog.getTheme(theme.getDesignation());
    Citizen c = citizenCatalog.getCitizen(nif);

    List<DelegateTheme> dt_list = dtCatalog.getAll();

    boolean exists = false;
    for (int i = 0; i < dt_list.size() && !exists; i++) {
      DelegateTheme dt = dt_list.get(i);
      if (dt.checkDelegateTheme(d, t)) {
        dt.addVoter(c);
        c.addDelegateTheme(dt);
      }
    }
    if (!exists) {
      DelegateTheme dt = new DelegateTheme(d, t);
      dt.addVoter(c);
      c.addDelegateTheme(dt);
      dtCatalog.addDT(dt);
    }
  }
}
