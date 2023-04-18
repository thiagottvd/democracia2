package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.DelegateDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ChooseDelegateHandler;

@Service
public class ChooseDelegateService {

  private final ChooseDelegateHandler chooseDelegateHandler;

  @Autowired
  public ChooseDelegateService(ChooseDelegateHandler chooseDelegateHandler) {
    this.chooseDelegateHandler = chooseDelegateHandler;
  }

  public List<DelegateDTO> getDelegates() {
    return chooseDelegateHandler.getDelegates();
  }

  public List<ThemeDTO> getThemes() {
    return chooseDelegateHandler.getThemes();
  }

  public void chooseDelegate(int delegateCc, String themeDesignation, int voterCc)
      throws ApplicationException {
    chooseDelegateHandler.chooseDelegate(delegateCc, themeDesignation, voterCc);
  }
}
