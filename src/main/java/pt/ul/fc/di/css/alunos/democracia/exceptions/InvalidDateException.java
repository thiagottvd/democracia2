package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The InvalidDateException is an exception that is thrown when a date is invalid. It is a subclass
 * of ApplicationException, which is itself a subclass of Exception.
 */
public class InvalidDateException extends ApplicationException {

  /**
   * Constructs a new InvalidDateException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public InvalidDateException(String message) {
    super(message);
  }
}
