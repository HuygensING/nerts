package nl.knaw.huygens.pergamon.nerts.text;

import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Removes brackets and parentheses.
 */
public class BracketFilter implements Function<String, String> {

  private static final Pattern PATTERN = Pattern.compile("[\\(\\)\\[\\]]");

  @Override
  public String apply(String text) {
    return PATTERN.matcher(text).replaceAll("");
  }

}
