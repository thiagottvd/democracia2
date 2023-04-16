package pt.ul.fc.di.css.alunos.democracia.exceptions;

public class ApplicationException extends Exception {

  /**
   * Constructs a new `ApplicationException` with the specified error message.
   *
   * @param message The error message.
   */
  public ApplicationException(String message) {
    super(message);
  }

  /**
   * Constructs a new `ApplicationException` with the specified error message and wrapped exception.
   *
   * @param message The error message.
   * @param e The wrapped exception.
   */
  public ApplicationException(String message, Exception e) {
    super(message, e);
  }
}
