package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The CitizenAlreadySupportsBillException is an exception that is thrown when a citizen is trying
 * to support a bill that they have already supported. It is a subclass of ApplicationException,
 * which is itself a subclass of Exception.
 */
public class CitizenAlreadySupportsBillException extends ApplicationException {

  /**
   * Constructs a new CitizenAlreadySupportsBillException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public CitizenAlreadySupportsBillException(String message) {
    super(message);
  }
}
