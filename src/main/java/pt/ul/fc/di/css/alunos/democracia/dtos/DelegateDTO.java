package pt.ul.fc.di.css.alunos.democracia.dtos;

public class DelegateDTO {

  private final String name;

  private final int cc;

  public DelegateDTO(String name, int cc) {
    this.name = name;
    this.cc = cc;
  }

  public int getCc() {
    return cc;
  }

  public String getName() {
    return name;
  }
}
