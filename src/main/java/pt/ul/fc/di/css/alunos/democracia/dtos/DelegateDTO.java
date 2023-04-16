package pt.ul.fc.di.css.alunos.democracia.dtos;

public class DelegateDTO {

  private final String name;

  private final Long id;

  public DelegateDTO(String name, Long id) {
    this.name = name;
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
