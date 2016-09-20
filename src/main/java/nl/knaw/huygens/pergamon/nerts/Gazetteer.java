package nl.knaw.huygens.pergamon.nerts;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import nl.knaw.huygens.pergamon.nerts.text.DiacriticsFilter;
import nl.knaw.huygens.pergamon.nerts.text.NERTextNormalizer;
import nl.knaw.huygens.pergamon.nerts.text.NameLinkFilter;
import nl.knaw.huygens.pergamon.support.file.CSVImporter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A collection of names for Named Entity Recognition.
 * Each name has a type (person, place, organization, ...),
 * an optional unique identifier and an aptional frequency count.
 */
public class Gazetteer implements Matcher {

  public static final Predicate<String> INVALID_KEY = key -> key == null || key.isEmpty() || key.endsWith("?");
  public static final Predicate<String> VALID_KEY = INVALID_KEY.negate();

  private static final Function<String, String> DEFAULT_NORMALIZER //
      = new DiacriticsFilter() //
          .andThen(new NameLinkFilter()) //
          .andThen(new NERTextNormalizer());

  private static final int MIN_ALPHA_CHARS = 2;

  private final Table<String, String, Integer> data;

  private Function<String, String> textNormalizer;
  private Matcher matcher;

  public Gazetteer() {
    textNormalizer = DEFAULT_NORMALIZER;
    data = TreeBasedTable.create();
  }

  public Gazetteer withTextNormalizer(Function<String, String> normalizer) {
    textNormalizer = normalizer;
    return this;
  }

  public Gazetteer withDefaultTextNormalizer() {
    return withTextNormalizer(DEFAULT_NORMALIZER);
  }

  public Gazetteer update(File file) throws Exception {
    GazetteerReader reader = new GazetteerReader(this);
    reader.handleFile(file, 3, false);
    System.out.printf("Gazetteer data size = %s%n", data.size());
    return this;
  }

  public Gazetteer update(String text, String type, String key) {
    return update(text, type, key, 1);
  }

  public Gazetteer update(String text, String type, String key, int count) {
    if (text.chars().filter(Character::isAlphabetic).count() < MIN_ALPHA_CHARS) {
      System.out.printf("Rejected %s '%s'%n", type, text);
    } else {
      doUpdate(text, type, key, count);
    }
    return this;
  }

  private void doUpdate(String text, String type, String key, int count) {
    String name = textNormalizer.apply(text);
    String tag = convertToTag(type, key);
    Integer value = data.get(name, tag);
    int newCount = (value == null) ? count : value.intValue() + count;
    data.put(name, tag, newCount);
  }

  private String convertToTag(String type, String key) {
    key = key.trim();
    return key.isEmpty() ? (type + "|?") : (type + "|" + key);
  }

  public Gazetteer buildMatcher(int minCount) {
    matcher = new DictionaryMatcher(getNames(minCount), false);
    return this;
  }

  public Set<String> getNames(int minCount) {
    Set<String> names = new HashSet<>();
    for (String name : data.rowKeySet()) {
      int count = data.row(name).values().stream().mapToInt(Integer::intValue).sum();
      if (count >= minCount) {
        names.add(name);
      }
    }
    return names;
  }

  @Override
  public List<Match> match(String text) {
    String normalized = textNormalizer.apply(text);
    if (normalized.length() == text.length()) {
      return matcher.match(normalized);
    } else {
      System.err.println("* Normalization failed for");
      System.err.println(text);
      System.err.println(normalized);
      // best effort...
      return matcher.match(text);
    }
  }

  public List<Ident> identify(String text) {
    String name = textNormalizer.apply(text);
    List<Ident> idents = data.row(name).entrySet().stream() //
        .filter(entry -> VALID_KEY.test(entry.getKey())) //
        .map(entry -> new Ident(entry.getKey(), entry.getValue())) //
        .sorted().collect(Collectors.toList());
    return normalizeScore(idents);
  }

  private List<Ident> normalizeScore(List<Ident> idents) {
    double sum = idents.stream().mapToDouble(Ident::getScore).sum();
    double factor = (sum > 0.0) ? (1.0 / sum) : 0.0;
    idents.forEach(ident -> ident.normalizeScore(factor));
    return idents;
  }

  // ---------------------------------------------------------------------------

  /**
   * Adds data from a gazeteer file to an existing gazetteer.
   */
  public static class GazetteerReader extends CSVImporter {

    private final Gazetteer gazetteer;

    public GazetteerReader(Gazetteer gazetteer) throws Exception {
      this.gazetteer = gazetteer;
    }

    @Override
    protected void handleLine(String[] items) throws Exception {
      int count = (items.length > 3) ? Integer.valueOf(items[3]) : 1;
      gazetteer.update(items[0], items[1], items[2], count);
    }
  }

}
