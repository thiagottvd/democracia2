package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.DelegateThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.DelegateDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ThemeNotFoundException;

@Component
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
        .map(delegate -> new DelegateDTO(delegate.getName(), delegate.getCc()))
        .collect(Collectors.toList());
  }

  public List<ThemeDTO> getThemes() {
    List<Theme> themes = themeCatalog.getThemes();
    return themes.stream()
        .map(theme -> new ThemeDTO(theme.getDesignation()))
        .collect(Collectors.toList());
  }

  public void chooseDelegate(int delegateCc, String themeDesignation, int voterCc)
      throws ApplicationException {

    Delegate d = citizenCatalog.getDelegate(delegateCc);
    if (d == null) {
      throw new CitizenNotFoundException("Delegate with cc" + delegateCc + " not found.");
    }
    Theme t = themeCatalog.getTheme(themeDesignation);
    if (t == null) {
      throw new ThemeNotFoundException(
          "Theme with designation " + themeDesignation + " not found.");
    }
    Optional<Citizen> c = citizenCatalog.getCitizenByCc(voterCc);
    if (c.isEmpty()) {
      throw new CitizenNotFoundException("Citizen with cc" + c.get().getCc() + " not found.");
    }

    List<DelegateTheme> dt_list = dtCatalog.getAll();

    boolean exists = false;
    for (int i = 0; i < dt_list.size() && !exists; i++) {
      DelegateTheme dt = dt_list.get(i);
      if (dt.checkDelegateTheme(d, t)) {
        exists = true;
        dt.addVoter(c.get());
        c.get().addDelegateTheme(dt);
      }
    }
    if (!exists) {
      DelegateTheme dt = new DelegateTheme(d, t);
      dt.addVoter(c.get());
      c.get().addDelegateTheme(dt);
      dtCatalog.addDT(dt);
    }
  }
}
