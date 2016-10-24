package nl.knaw.huygens.pergamon.nerts.matcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * DictionaryMatcher that uses regular expressions.
 */
class REDictionaryMatcher implements Matcher {

  private final Pattern pattern;

  public REDictionaryMatcher(Set<String> names) {
    pattern = buildRE(names);
  }

  private Pattern buildRE(Set<String> names) {
    if (names.isEmpty()) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("\\b(?:");

    // Sort by reverse length to get longest match behavior.
    names.stream().sorted(Comparator.comparingInt(name -> -name.length())).forEach(name -> {
      sb.append(Pattern.quote(name));
      sb.append('|');
    });
    sb.deleteCharAt(sb.length() - 1); // Last '|' causes RE to match empty string.
    sb.append(")\\b");
    return Pattern.compile(sb.toString());
  }

  @Override
  public List<Match> match(String text) {
    if (pattern == null) {
      return Collections.emptyList();
    }
    java.util.regex.Matcher matcher = pattern.matcher(text);
    List<Match> result = new ArrayList<>();
    while (matcher.find()) {
      result.add(new Match(matcher.start(), matcher.end()));
    }
    return result;
  }

}
