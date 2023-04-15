package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.handlers.ListActivePollsHandler;

@Service
public class ListActivePollsService {

  private final ListActivePollsHandler listActivePollsHandler;

  public ListActivePollsService(ListActivePollsHandler listActivePollsHandler) {
    this.listActivePollsHandler = listActivePollsHandler;
  }

  public List<BillDTO> getActivePolls() {
    return listActivePollsHandler.getActivePolls();
  }
}
