package pt.ul.fc.di.css.alunos.democracia.dtos;

import java.time.LocalDate;

/** A Data Transfer Object (DTO) representing a Poll. */
public class PollDTO {

  private final String title;
  private final LocalDate expirationDate;

  /**
   * Constructs a PollDTO object from a Poll object.
   * @param title The associated bill title.
   * @param expirationDate The poll expiration date.
   */
  public PollDTO(String title, LocalDate expirationDate) {
    this.title = title;
    this.expirationDate = expirationDate;
  }

  /***** GETTERS *****/

  public String getTitle() {
    return title;
  }
}
