package pt.ul.fc.di.css.alunos.democracia.dtos;

public class DelegateDTO {

  private final String name;

  private int nif;

  public DelegateDTO(String name, int nif) {
    this.name = name;
    this.nif = nif;
  }

  public int getNif() {
    return nif;
  }

  public String getName() {
    return name;
  }
}
