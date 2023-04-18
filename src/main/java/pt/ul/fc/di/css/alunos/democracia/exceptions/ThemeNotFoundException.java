package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The ThemeNotFoundException is an exception that is thrown when a theme cannot be found. It is a
 * subclass of ApplicationException, which is itself a subclass of Exception.
 */
public class ThemeNotFoundException extends ApplicationException {

  /**
   * Constructs a new BillNotFoundException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public ThemeNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new BillNotFoundException with the specified error message and a reference to the
   * lower-level exception that caused this exception.
   *
   * @param message the error message to be associated with the exception.
   * @param e the lower-level exception that caused this exception to be thrown.
   */
  public ThemeNotFoundException(String message, Exception e) {
    super(message, e);
  }
}
