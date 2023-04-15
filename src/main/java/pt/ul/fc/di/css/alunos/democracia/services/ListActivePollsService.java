package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;

@Service
public interface ListActivePollsService {
  List<BillDTO> getActivePolls();
}
