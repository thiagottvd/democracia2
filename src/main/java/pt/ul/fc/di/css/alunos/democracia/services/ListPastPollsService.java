package pt.ul.fc.di.css.alunos.democracia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.PollDTO;
import pt.ul.fc.di.css.alunos.democracia.handlers.ListPastPollsHandler;

import java.util.List;

/**
 * Use case C.
 *
 * <p>Service class that retrieves a list of the polls that are inactive (rejected or approved polls)
 * calling the appropriate handler {@link ListPastPollsHandler} to retrieve the inactive polls and converting
 * them into a list of PollDTO objects.
 */
@Service
public class ListPastPollsService {

    private final ListPastPollsHandler listPastPollsHandler;

    /**
     * Constructor for the ListPastPollsService class. It takes a ListPastPollsHandler object as
     * parameter and sets it as an attribute.
     *
     * @param listPastPollsHandler the handler responsible for retrieving the inactive polls.
     */
    @Autowired
    public ListPastPollsService(ListPastPollsHandler listPastPollsHandler) {
        this.listPastPollsHandler = listPastPollsHandler;
    }

    /**
     * Returns a list of inactive polls in the form of PollDTO objects.
     *
     * @return a list of inactive polls.
     */
    public List<PollDTO> getInactivePolls() {
        return listPastPollsHandler.getInactivePolls();
    }

}
