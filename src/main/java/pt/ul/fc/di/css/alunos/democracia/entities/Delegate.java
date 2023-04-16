package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.Entity;

@Entity
// @DiscriminatorValue("Delegate")
public class Delegate extends Citizen {

  protected Delegate() {
    // Empty constructor required by JPA.
  }

  public Delegate(String name, int nif) {
    super(name, nif);
  }
}
