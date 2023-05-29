package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.lang.NonNull;

/** Represents a citizen (voter). */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Citizen {
  @Id @GeneratedValue private long id;
  @NonNull private String name;

  @Column(unique = true)
  @NonNull
  private Integer citizenCardNumber;

  @ManyToMany(mappedBy = "voters", fetch = FetchType.EAGER)
  @Cascade(CascadeType.ALL)
  private final List<DelegateTheme> delegateThemes = new ArrayList<>();

  protected Citizen() {
    // Empty constructor required by JPA.
  }

  /**
   * Represents a citizen with a name and a citizen card number (citizenCardNumber).
   *
   * @param name the name of the citizen (non-null).
   * @param citizenCardNumber the citizen card number (non-null).
   */
  public Citizen(@NonNull String name, @NonNull Integer citizenCardNumber) {
    this.name = name;
    this.citizenCardNumber = citizenCardNumber;
  }

  /**
   * Returns the citizen id number.
   *
   * @return the citizen id number.
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the list of DelegateThemes objects associated with this citizen.
   *
   * @return the list of DelegateThemes objects associated with this citizen.
   */
  public List<DelegateTheme> getDelegateThemes() {
    return delegateThemes;
  }

  /**
   * Adds a delegate theme to the list of themes that this citizen represents, if not already
   * represented.
   *
   * @param dt The delegate theme to add.
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
  @NonNull
  public String getName() {
    return name;
  }

  /**
   * Returns the citizen card number.
   *
   * @return the citizen card number.
   */
  @NonNull
  public Integer getCitizenCardNumber() {
    return citizenCardNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Citizen citizen = (Citizen) o;
    return Objects.equals(id, citizen.id)
        && Objects.equals(citizenCardNumber, citizen.citizenCardNumber)
        && Objects.equals(name, citizen.name);
  }

  /**
   * Removes a delegate theme from the list of delegate themes.
   *
   * @param delegateTheme the delegate theme to remove.
   */
  public void removeDelegateTheme(DelegateTheme delegateTheme) {
    this.delegateThemes.remove(delegateTheme);
  }
}
