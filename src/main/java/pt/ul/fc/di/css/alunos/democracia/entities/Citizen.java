package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.List;

@Entity
public class Citizen {
  @Id @GeneratedValue private long id;

  private String name;

  private int cc;

  @ManyToMany(mappedBy = "voters")
  private List<DelegateTheme> delegateThemes;

  public Long getId() {
    return id;
  }

  protected Citizen() {
    // Empty constructor required by JPA.
  }

  public Citizen(String name, int cc) {
    this.name = name;
    this.cc = cc;
  }

  public List<DelegateTheme> getDelegateThemes() {
    return delegateThemes;
  }
  /*
   Checks if the chosen DelegateTheme already represents this citizen
  */
  public void addDelegateTheme(DelegateTheme dt) {
    boolean alreadyRepresents = false;
    for (int i = 0; i < delegateThemes.size() && !alreadyRepresents; i++) {
      if (dt.getTheme()
          .getDesignation()
          .equals(delegateThemes.get(i).getTheme().getDesignation())) {
        if (dt.getDelegate().getId().equals(delegateThemes.get(i).getDelegate().getId())) {
          alreadyRepresents = true;
        }
      }
    }
    if (!alreadyRepresents) {
      delegateThemes.add(dt);
    }
  }
  /**
   * Returns the citizen name.
   *
   * @return the citizen name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the citizen card number.
   *
   * @return the citizen card number.
   */
  public int getCc() {
    return cc;
  }
}
