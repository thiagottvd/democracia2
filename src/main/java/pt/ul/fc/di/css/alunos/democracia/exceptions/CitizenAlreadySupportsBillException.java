package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The CitizenAlreadySupportsBillException is an exception that is thrown when a citizen is trying
 * to support a bill that they have already supported. It is a subclass of ApplicationException,
 * which is itself a subclass of Exception.
 */
public class CitizenAlreadySupportsBillException extends ApplicationException {

  /**
   * Constructs a new BillNotFoundException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public CitizenAlreadySupportsBillException(String message) {
    super(message);
  }

  /**
   * Constructs a new BillNotFoundException with the specified error message and a reference to the
   * lower-level exception that caused this exception.
   *
   * @param message the error message to be associated with the exception.
   * @param e the lower-level exception that caused this exception to be thrown.
   */
  public CitizenAlreadySupportsBillException(String message, Exception e) {
    super(message, e);
  }
}
