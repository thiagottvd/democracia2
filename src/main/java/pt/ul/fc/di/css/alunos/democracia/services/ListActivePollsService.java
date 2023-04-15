package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.handlers.ListActivePollsHandler;

@Service
public class ListActivePollsService {

  private final ListActivePollsHandler listActivePollsHandler;

  @Autowired
  public ListActivePollsService(ListActivePollsHandler listActivePollsHandler) {
    this.listActivePollsHandler = listActivePollsHandler;
  }

  public List<PollDTO> getActivePolls() {
    return listActivePollsHandler.getActivePolls();
  }
}
