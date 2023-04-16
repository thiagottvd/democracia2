package pt.ul.fc.di.css.alunos.democracia.exceptions;

public class BillNotFoundException extends ApplicationException {

  /**
   * Creates an exception given an error message.
   *
   * @param message The error message.
   */
  public BillNotFoundException(String message) {
    super(message);
  }

  /**
   * Creates an exception wrapping a lower level exception.
   *
   * @param message The error message.
   * @param e The wrapped exception.
   */
  public BillNotFoundException(String message, Exception e) {
    super(message, e);
  }
}
