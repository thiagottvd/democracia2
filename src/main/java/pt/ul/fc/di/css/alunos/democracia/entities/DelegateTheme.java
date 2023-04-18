package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DelegateTheme {

  @Id @GeneratedValue private Long id;

  @OneToOne private Theme theme;

  @OneToOne private Delegate delegate;

  @ManyToMany private List<Citizen> voters;

  protected DelegateTheme() {
    // Empty constructor required by JPA.
  }

  /**
   * DelegateTheme class constructor.
   * @param delegate The associated delegate.
   * @param theme The associated theme.
   */
  public DelegateTheme(Delegate delegate, Theme theme) {
    this.theme = theme;
    this.delegate = delegate;
    voters = new ArrayList<>();
  }

  /**
   * Adds a new voter to the object voters list.
   * @param citizen The citizen to add.
   */
  public void addVoter(Citizen citizen) { voters.add(citizen); }

  /**
   * Checks if the Theme given is the same as the attribute.
   * @param t The given theme.
   * @return True or false accordingly.
   */
  public boolean checkTheme(Theme t) {
    return theme.getDesignation().equals(t.getDesignation());
  }

  /**
   * Checks if the Delegate and Theme given are the same as the attributes.
   * @param d The given delegate.
   * @param t The given theme.
   * @return True or false accordingly.
   */
  public boolean checkDelegateTheme(Delegate d, Theme t) {
    return d.getId().equals(delegate.getId()) && t.getDesignation().equals(theme.getDesignation());
  }

  /***** GETTERS *****/

  public Delegate getDelegate() {
    return delegate;
  }

  public Theme getTheme() {
    return theme;
  }

  public List<Citizen> getVoters() {
    return voters;
  }
}
