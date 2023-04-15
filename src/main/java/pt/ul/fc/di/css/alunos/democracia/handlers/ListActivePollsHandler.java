package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;

@Component
public class ListActivePollsHandler {

  private final PollRepository pollRepository;

  public ListActivePollsHandler(PollRepository pollRepository) {
    this.pollRepository = pollRepository;
  }

  public List<BillDTO> getActivePolls() {
    List<Poll> activePolls = pollRepository.findAllActivePolls(PollStatus.ACTIVE);
    return activePolls.stream()
        .map(poll -> new BillDTO(poll.getBill()))
        .collect(Collectors.toList());
  }
}
