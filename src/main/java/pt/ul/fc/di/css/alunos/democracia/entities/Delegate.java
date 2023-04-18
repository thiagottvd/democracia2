package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.Entity;

@Entity
// @DiscriminatorValue("Delegate")
public class Delegate extends Citizen {

  protected Delegate() {
    // Empty constructor required by JPA.
  }

  /**
   * Delegate class constructor.
   * @param name The delegate name.
   * @param cc The delegate cc.
   */
  public Delegate(String name, int cc) {
    super(name, cc);
  }
}
