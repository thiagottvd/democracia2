package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;

@Component
public class ListActivePollsHandler {

  private final PollCatalog pollCatalog;

  @Autowired
  public ListActivePollsHandler(PollCatalog pollCatalog) {
    this.pollCatalog = pollCatalog;
  }

  public List<PollDTO> getActivePolls() {
    List<Poll> activePolls = pollCatalog.getPollsByStatusType(PollStatus.ACTIVE);
    return activePolls.stream()
        .map(poll -> new PollDTO(poll.getBill().getTitle(), poll.getBill().getExpirationDate()))
        .collect(Collectors.toList());
  }
}
