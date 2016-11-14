package nl.knaw.huygens.pergamon.nerts;

import static java.util.Comparator.comparing;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

import com.google.common.collect.Lists;

import nl.knaw.huygens.pergamon.support.file.CSVImporter;

/**
 * A collection of named entities.
 *
 * Allows customization by specifying a name normalizer and a name generator;
 */
public class NamedEntities {

  private static final int MIN_ALPHA_CHARS = 2;

  private static final UnaryOperator<String> DEFAULT_NORMALIZER = String::trim;
  private static final NameGenerator DEFAULT_GENERATOR = Lists::newArrayList;

  private final Map<NamedEntity, Integer> entities;
  private UnaryOperator<String> nameNormalizer;
  private NameGenerator nameGenerator;

  /**
   * Creates a {@code NamedEntities} instance.
   */
  public NamedEntities() {
    entities = new HashMap<>();
    nameNormalizer = DEFAULT_NORMALIZER;
    nameGenerator = DEFAULT_GENERATOR;
  }

  /**
   * Sets the specified name normalizer.
   */
  public NamedEntities withNameNormalizer(UnaryOperator<String> normalizer) {
    nameNormalizer = Objects.requireNonNull(normalizer);
    return this;
  }

  /**
   * Sets the default name normalizer.
   */
  public NamedEntities withDefaultNameNormalizer() {
    return withNameNormalizer(DEFAULT_NORMALIZER);
  }

  /**
   * Sets the specified name generator.
   */
  public NamedEntities withNameGenerator(NameGenerator generator) {
    nameGenerator = Objects.requireNonNull(generator);
    return this;
  }

  /**
   * Creates a named entity and adds it to this entities.
   * Note that the name normalizer is applied before the name generator.
   */
  public void add(String text, String type, String id, int count) {
    if (text.chars().filter(Character::isAlphabetic).count() < MIN_ALPHA_CHARS) {
      System.out.printf("Rejected %s '%s'%n", type, text);
    } else {
      String normalizedId = (id == null || id.isEmpty()) ? "?" : id;
      String normalizedText = nameNormalizer.apply(text);
      nameGenerator.apply(normalizedText).forEach(name -> {
        NamedEntity key = new NamedEntity(name, type, normalizedId);
        entities.put(key, entities.getOrDefault(key, 0) + count);
      });
    }
  }

  public int size() {
    return entities.size();
  }

  public Gazetteer buildGazetteer() {
    Gazetteer gazetteer = new Gazetteer().withDefaultTextNormalizer();
    // XXX Do we need sorted order here?
    forEachEntrySorted((ne, value) -> gazetteer.update(ne.getName(), ne.getType(), ne.getId(), value));
    return gazetteer;
  }

  private void forEachEntrySorted(BiConsumer<NamedEntity, Integer> consumer) {
    entities.entrySet().parallelStream().sorted(comparing(Map.Entry::getKey)).forEachOrdered(x -> consumer.accept(x.getKey(), x.getValue()));
  }

  private static final String FORMAT = "\"%s\";\"%s\";\"%s\";\"%d\"%n";

  /**
   * Writes the named entities in this entities to the specified file.
   */
  public void writeToFile(File file) throws Exception {
    PrintWriter writer = new PrintWriter(file, "UTF-8");
    writeHeader(writer);
    forEachEntrySorted((ne, value) -> writer.format(FORMAT, ne.getName(), ne.getType(), ne.getId(), value));
    writer.close();
  }

  private void writeHeader(PrintWriter writer) {
    writer.format("--%n");
    writer.format("-- Named entities%n");
    writer.format("-- %s%n", new Date());
    writer.format("--%n");
  }

  // ---------------------------------------------------------------------------

  /**
   * Reads named entities from the specified file
   * and adds them to this {@code NamedEntities} instance.
   */
  public NamedEntities readFromFile(File file) throws Exception {
    new NamedEntityReader().handleFile(file, 3, false);
    return this;
  }

  private class NamedEntityReader extends CSVImporter {
    @Override
    protected void handleLine(String[] items) throws Exception {
      int count = (items.length > 3) ? Integer.valueOf(items[3]) : 1;
      add(items[0], items[1], items[2], count);
    }
  }

}
