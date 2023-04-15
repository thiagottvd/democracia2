package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Delegate implements Citizen {

  @Id @GeneratedValue private Long id;

  public Long getId() {
    return id;
  }

  protected Delegate() {
    // Empty constructor required by JPA.
  }
}
