package pt.ul.fc.di.css.alunos.democracia.dtos;

import java.time.LocalDate;

public class PollDTO {

  private final String title;
  private final LocalDate expirationDate;

  public PollDTO(String title, LocalDate expirationDate) {
    this.title = title;
    this.expirationDate = expirationDate;
  }
}
