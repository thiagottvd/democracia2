package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that represents the association between a delegate, a theme, and the citizens who are
 * represented by the delegate for that particular theme.
 */
@Entity
public class DelegateTheme {

  @Id @GeneratedValue private Long id;

  @ManyToOne private Theme theme;

  @ManyToOne private Delegate delegate;

  @ManyToMany private List<Citizen> voters;

  protected DelegateTheme() {
    // Empty constructor required by JPA.
  }

  /**
   * Constructs a new DelegateTheme instance with the given delegate and theme.
   *
   * @param delegate the delegate associated with this delegate-theme association.
   * @param theme the theme associated with this delegate-theme association.
   */
  public DelegateTheme(Delegate delegate, Theme theme) {
    this.theme = theme;
    this.delegate = delegate;
    voters = new ArrayList<>();
  }

  /**
   * Gets the delegate associated with this delegate-theme association.
   *
   * @return the delegate associated with this delegate-theme association.
   */
  public Delegate getDelegate() {
    return delegate;
  }

  /**
   * Gets the theme associated with this delegate-theme association.
   *
   * @return the theme associated with this delegate-theme association.
   */
  public Theme getTheme() {
    return theme;
  }

  /**
   * Adds a citizen to the list of citizens represented by the delegate for this particular theme.
   *
   * @param citizen the citizen to be added to the list of voters.
   */
  public void addVoter(Citizen citizen) {
    voters.add(citizen);
  }

  /**
   * Gets the list of citizens represented by the delegate for this particular theme.
   *
   * @return the list of citizens represented by the delegate for this particular theme.
   */
  public List<Citizen> getVoters() {
    return voters;
  }

  /**
   * Checks if the given theme is the same as the theme associated with this delegate-theme
   * association.
   *
   * @param t the theme to be checked.
   * @return true if the given theme is the same as the theme associated with this delegate-theme
   *     association; false otherwise.
   */
  public boolean checkTheme(Theme t) {
    return theme.getDesignation().equals(t.getDesignation());
  }

  /**
   * Returns the ID of this delegate-theme association.
   *
   * @return the ID of this delegate-theme association.
   */
  public Long getId() {
    return id;
  }

  /**
   * Checks if the given delegate and theme are the same as the delegate and theme associated with
   * this delegate-them association.
   *
   * @param d the delegate to be checked.
   * @param t the theme to be checked.
   * @return true if the given delegate and theme are the same as the delegate and theme associated
   *     with this delegate-theme association; false otherwise.
   */
  public boolean checkDelegateTheme(Delegate d, Theme t) {
    return d.getId().equals(delegate.getId()) && t.getDesignation().equals(theme.getDesignation());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DelegateTheme delegateTheme = (DelegateTheme) o;
    return Objects.equals(id, delegateTheme.id)
        && Objects.equals(theme.getDesignation(), delegateTheme.theme.getDesignation())
        && Objects.equals(delegate.getId(), delegateTheme.theme.getId());
  }

  /**
   * Removes a citizen from the list of citizens represented by the delegate for this particular
   * theme.
   *
   * @param citizen the citizen to be removed from the list of voters.
   */
  public void removeCitizenRep(Citizen citizen) {
    this.voters.remove(citizen);
  }
}
