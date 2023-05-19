package pt.ul.fc.di.css.alunos.democracia.datatypes;

/**
 * An enumeration representing the possible states of a poll in the new election model proposed.
 * "ACTIVE" means that a poll is open and accepting votes. "APPROVED" means that the poll is closed,
 * is no longer accepting votes, and the associated bill was approved. "REJECTED" means that the
 * poll is closed, is no longer accepting votes, and the associated bill was rejected.
 */
public enum PollStatus {
  ACTIVE,
  APPROVED,
  REJECTED
}
