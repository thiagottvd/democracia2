package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.util.List;
import java.util.stream.Collectors;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ListActivePollsService;

public class ListActivePollsHandler implements ListActivePollsService {

  private final PollRepository pollRepository;

  public ListActivePollsHandler(PollRepository pollRepository) {
    this.pollRepository = pollRepository;
  }

  @Override
  public List<BillDTO> getActivePolls() {
    List<Poll> activePolls = pollRepository.findAllActivePolls(PollStatus.ACTIVE);
    return activePolls.stream()
        .map(poll -> new BillDTO(poll.getBill()))
        .collect(Collectors.toList());
  }
}
