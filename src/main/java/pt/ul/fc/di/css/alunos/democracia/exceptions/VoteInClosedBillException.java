package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The VoteInClosedBillException is an exception that is thrown when a vote is attempted on a closed
 * bill. It is a subclass of ApplicationException, which is itself a subclass of Exception.
 */
public class VoteInClosedBillException extends ApplicationException {

  /**
   * Constructs a new VoteInClosedBillException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public VoteInClosedBillException(String message) {
    super(message);
  }
}
