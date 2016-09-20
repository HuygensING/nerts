package nl.knaw.huygens.pergamon.nerts;

import java.util.Locale;

/**
 * Represents a name identification.
 */
public class Ident implements Comparable<Ident> {

  private static final String REGEX = "\\|";

  private final String type;
  private final String id;
  private double score;

  public Ident(String type, String id, double score) {
    this.type = type;
    this.id = id;
    this.score = score;;
  }

  public Ident(String typeAndId, double score) {
    String[] items = typeAndId.split(REGEX);
    type = items[0];
    id = (items.length > 1) ? items[1] : "?";
    this.score = score;
  }

  public Ident(String typeAndIdAndScore) {
    String[] items = typeAndIdAndScore.split(REGEX);
    type = items[0];
    id = (items.length > 1) ? items[1] : "?";
    score = (items.length > 2) ? Double.parseDouble(items[2]) : 0.0;
  }

  public String getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public double getScore() {
    return score;
  }

  public void normalizeScore(double factor) {
    score *= factor;
  }

  @Override
  public String toString() {
    return String.format(Locale.US, "%s|%s|%.3f", type, id, score);
  }

  @Override
  public int compareTo(Ident that) {
    return Double.compare(that.score, this.score);
  }

}
