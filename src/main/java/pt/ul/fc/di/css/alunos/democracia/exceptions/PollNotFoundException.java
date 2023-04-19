package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The PollNotFoundException is an exception that is thrown when a poll can not be found. It is a
 * subclass of ApplicationException, which is itself a subclass of Exception.
 */
public class PollNotFoundException extends ApplicationException {

  /**
   * Constructs a new PollNotFoundException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public PollNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new PollNotFoundException with the specified error message and a reference to the
   * lower-level exception that caused this exception.
   *
   * @param message the error message to be associated with the exception.
   * @param e the lower-level exception that caused this exception to be thrown.
   */
  public PollNotFoundException(String message, Exception e) {
    super(message, e);
  }
}
