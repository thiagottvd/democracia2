package pt.ul.fc.di.css.alunos.democracia.entities;

import static jakarta.persistence.InheritanceType.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.lang.NonNull;

@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
public class Citizen {
  @Id @GeneratedValue private long id;
  @NonNull private String name;

  @Column(unique = true)
  @NonNull
  private Integer cc;

  @ManyToMany(mappedBy = "voters")
  @Cascade(CascadeType.ALL)
  private final List<DelegateTheme> delegateThemes = new ArrayList<>();

  public Long getId() {
    return id;
  }

  protected Citizen() {
    // Empty constructor required by JPA.
  }

  public Citizen(@NonNull String name, @NonNull Integer cc) {
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
  public Integer getCc() {
    return cc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Citizen citizen = (Citizen) o;
    return Objects.equals(id, citizen.id)
        && Objects.equals(cc, citizen.cc)
        && Objects.equals(name, citizen.name);
  }

  public void removeDelegateTheme(DelegateTheme dt) {
    this.delegateThemes.remove(dt);
  }
}
