package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class DelegateTheme {

  @Id @GeneratedValue private Long id;

  @OneToOne private Theme theme;

  @OneToOne private Delegate delegate;

  @ManyToMany private List<Citizen> voters;

  protected DelegateTheme() {
    // Empty constructor required by JPA.
  }

  public DelegateTheme(Delegate delegate, Theme theme) {
    this.theme = theme;
    this.delegate = delegate;
    voters = new ArrayList<>();
  }

  public Delegate getDelegate() {
    return delegate;
  }

  public Theme getTheme() {
    return theme;
  }

  public void addVoter(Citizen citizen) {

    voters.add(citizen);
  }

  public List<Citizen> getVoters() {
    return voters;
  }

  public boolean checkTheme(Theme t) {
    return theme.getDesignation().equals(t.getDesignation());
  }

  public Long getId() {
    return id;
  }

  /*
     Checks if the Delegate and Theme given are the same as the attributes
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
}
