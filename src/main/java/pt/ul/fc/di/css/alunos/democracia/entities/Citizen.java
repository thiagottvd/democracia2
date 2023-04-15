package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Citizen {
  @Id @GeneratedValue private long id;

  private String name;

  private int nif;

  @ManyToMany
  // private List<DelegateTheme> delegateThemes;

  public Long getId() {
    return id;
  }

  protected Citizen() {
    // Empty constructor required by JPA.
  }
}
