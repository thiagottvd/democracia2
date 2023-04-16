package pt.ul.fc.di.css.alunos.democracia.exceptions;

public class BillNotFoundException extends ApplicationException {

  /**
   * Constructs a new BillNotFoundException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public BillNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new BillNotFoundException with the specified error message and a reference to the
   * lower-level exception that caused this exception.
   *
   * @param message the error message to be associated with the exception.
   * @param e the lower-level exception that caused this exception to be thrown.
   */
  public BillNotFoundException(String message, Exception e) {
    super(message, e);
  }
}
