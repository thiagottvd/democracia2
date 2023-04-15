package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class Bill {

  @Id @GeneratedValue private Long id;
  private String title;
  private LocalDate expirationDate;
  @OneToOne private Poll associatedPoll;

  protected Bill() {
    // Empty constructor required by JPA.
  }

  public Bill(String title, LocalDate expirationDate) {
    this.title = title;
    this.expirationDate = expirationDate;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }
}
