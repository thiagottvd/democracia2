package pt.ul.fc.di.css.alunos.democracia.dtos;

/** A data transfer object (DTO) representing a poll. */
public class PollDTO {

  private final String title;

  /**
   * Constructs a new PollDTO with the given title.
   *
   * @param title the title of the poll.
   */
  public PollDTO(String title) {
    this.title = title;
  }

  /**
   * Returns the title of the poll.
   *
   * @return the title of the poll.
   */
  public String getTitle() {
    return title;
  }
}
