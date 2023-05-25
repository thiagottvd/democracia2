package pt.ul.fc.di.css.alunos.democracia.dtos;

/** A data transfer object (DTO) representing a poll. */
public class PollDTO {

  private final Long id;
  private final String title;

  /**
   * Constructs a new PollDTO with the given title.
   *
   * @param id the poll ID.
   * @param title the title of the poll.
   */
  public PollDTO(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  /**
   * Returns the ID of the poll.
   *
   * @return the ID of the poll.
   */
  public Long getId() {
    return id;
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
