package pt.ul.fc.di.css.alunos.democracia.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;

@Component
public class PollCatalog {

  private final PollRepository pollRepository;

  @Autowired
  public PollCatalog(PollRepository pollRepository) {
    this.pollRepository = pollRepository;
  }

  public List<Poll> getActivePolls() {
    return pollRepository.findAllActivePolls(PollStatus.ACTIVE);
  }

  public void savePoll(Poll poll) {
    pollRepository.save(poll);
  }
}
