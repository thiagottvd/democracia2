package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The ThemeNotFoundException is an exception that is thrown when a theme cannot be found. It is a
 * subclass of ApplicationException, which is itself a subclass of Exception.
 */
public class ThemeNotFoundException extends ApplicationException {

  /**
   * Constructs a new ThemeNotFoundException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public ThemeNotFoundException(String message) {
    super(message);
  }
}
