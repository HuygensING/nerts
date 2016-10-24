package nl.knaw.huygens.pergamon.nerts.matcher;

/**
 * A match of a pattern in a text
 */
public class Match {

  private final int start;
  private final int end;
  private final String type;
  private final double score;

  public Match(int start, int end, String type, double score) {
    this.start = start;
    this.end = end;
    this.type = type;
    this.score = score;
  }

  public Match(int start, int end, String type) {
    this.start = start;
    this.end = end;
    this.type = type;
    this.score = 1.0;
  }

  public Match(int start, int end) {
    this.start = start;
    this.end = end;
    this.type = "MATCH";
    this.score = 1.0;
  }

  /**
   * Returns the index of the first character of this match.
   */
  public int getStart() {
    return start;
  }

  /**
   * Returns the index of one past the last character of this match.
   */
  public int getEnd() {
    return end;
  }

  /**
   * Returns the type of this match.
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the score of this match.
   */
  public double getScore() {
    return score;
  }

}
