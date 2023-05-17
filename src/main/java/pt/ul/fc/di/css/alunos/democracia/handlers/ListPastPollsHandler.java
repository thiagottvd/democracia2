package pt.ul.fc.di.css.alunos.democracia.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Poll;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles use case C.
 *
 * <p>Handler that retrieves a list of inactive (approved or rejected) polls and converts them to a list of
 * {@link PollDTO} objects.
 */
@Component
public class ListPastPollsHandler {

    private final PollCatalog pollCatalog;

    /**
     * Constructor for the ListPastPollsHandler class. It takes a PollCatalog object as parameter
     * and sets it as an attribute.
     *
     * @param pollCatalog the catalog responsible for managing polls.
     */
    @Autowired
    public ListPastPollsHandler(PollCatalog pollCatalog) {
        this.pollCatalog = pollCatalog;
    }

    /**
     * Returns a list of inactive polls in the form of PollDTO objects.
     *
     * @return a list of inactive polls.
     */
    public List<PollDTO> getInactivePolls() {
        List<Poll> inactivePolls = pollCatalog.getPollsByStatusType(PollStatus.REJECTED);
        inactivePolls.addAll(pollCatalog.getPollsByStatusType(PollStatus.APPROVED));
        return inactivePolls.stream()
                .map(poll -> new PollDTO(poll.getAssociatedBill().getTitle()))
                .collect(Collectors.toList());
    }
}
