package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The CitizenNotFoundException is an exception that is thrown when a citizen (includes delegate)
 * cannot be found. It is a subclass of ApplicationException, which is itself a subclass of
 * Exception.
 */
public class CitizenNotFoundException extends ApplicationException {

  /**
   * Constructs a new BillNotFoundException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public CitizenNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new BillNotFoundException with the specified error message and a reference to the
   * lower-level exception that caused this exception.
   *
   * @param message the error message to be associated with the exception.
   * @param e the lower-level exception that caused this exception to be thrown.
   */
  public CitizenNotFoundException(String message, Exception e) {
    super(message, e);
  }
}
