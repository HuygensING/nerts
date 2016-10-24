package nl.knaw.huygens.pergamon.nerts.tool;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import nl.knaw.huygens.pergamon.nerts.Gazetteer;
import nl.knaw.huygens.pergamon.nerts.NamedEntities;
import nl.knaw.huygens.pergamon.nerts.TextAnnotator;
import nl.knaw.huygens.pergamon.nerts.lang.LanguageAssignerVisitor;
import nl.knaw.huygens.pergamon.nerts.lang.LanguageIdentifier;
import nl.knaw.huygens.pergamon.nerts.matcher.MatcherType;
import nl.knaw.huygens.pergamon.nerts.tei.AttributeRemover;
import nl.knaw.huygens.pergamon.nerts.tei.TeiAnnotationVisitor;
import nl.knaw.huygens.pergamon.nerts.tei.TeiNameCollector;
import nl.knaw.huygens.pergamon.nerts.tei.TeiNameIdentifier;
import nl.knaw.huygens.pergamon.nerts.tei.TeiNameResolver;
import nl.knaw.huygens.pergamon.nerts.tei.TeiPredicates;
import nl.knaw.huygens.pergamon.support.tei.Attributes;
import nl.knaw.huygens.pergamon.support.tei.Documents;
import nl.knaw.huygens.pergamon.support.tei.export.ExportVisitor;
import nl.knaw.huygens.tei.Document;
import nl.knaw.huygens.tei.Element;

/**
 * NER annotation tool that processes XML-documents.
 * It has two modes of operation:
 * - if you don't supply a gazetteer, one will be created by scanning the document for NER annotations,
 * and that gazetteer will be used.
 * - if you supply a gazetteer it will be used;
 * It is expected that the first mode will provide fewer, but more reliable annotations.
 * Thus, you can use the tool in two passes and have the best of two worlds.
 */
public class TeiAnnotator implements UnaryOperator<String> {

  /**
   * Element name of generated named entity annotations.
   */
  public static final String ELEMENT_NAME = "NAME";

  private static final String OPEN_TAG = "<" + ELEMENT_NAME + ">";
  private static final String CLOSE_TAG = "</" + ELEMENT_NAME + ">";

  private static final String TEMP_LANG_ATTR = "LANGUAGE";

  private final Predicate<Element> hasNoKeysAttribute = element -> !element.hasAttribute(Attributes.KEYS);

  private LanguageIdentifier languageIdentifier;
  private Gazetteer globalGazetteer;
  private String teiAnnotatorName;
  private TextAnnotator textAnnotator;

  private final String[] elementNames;

  public TeiAnnotator(String... elementNames) {
    languageIdentifier = null;
    globalGazetteer = null;
    teiAnnotatorName = null;
    textAnnotator = new TextAnnotator();
    this.elementNames = elementNames;
  }

  public TeiAnnotator withLanguageIdentifier(LanguageIdentifier identifier) {
    languageIdentifier = Objects.requireNonNull(identifier);
    return this;
  }

  public TeiAnnotator withGazetteer(Gazetteer gazetteer) {
    globalGazetteer = Objects.requireNonNull(gazetteer);
    return this;
  }

  public TeiAnnotator withTeiAnnotatorName(String annotatorName) {
    teiAnnotatorName = Objects.requireNonNull(annotatorName);
    return this;
  }

  public TeiAnnotator withTextAnnotator(TextAnnotator annotator) {
    textAnnotator = annotator;
    return this;
  }

  @Override
  public String apply(String xml) {
    try {
      Document document = Documents.newDocument(xml);

      Gazetteer gazetteer = getGazetteer(document);
      if (languageIdentifier != null) {
        document.accept(new LanguageAssignerVisitor(languageIdentifier, TEMP_LANG_ATTR));
      }
      document.accept(new TeiAnnotationVisitor(TeiPredicates.isAnnotated, gazetteer, textAnnotator, TEMP_LANG_ATTR));
      if (languageIdentifier != null) {
        document.accept(new AttributeRemover(TEMP_LANG_ATTR));
      }
      document = convertNameTags(document);
      document.accept(new TeiNameIdentifier(gazetteer, hasNoKeysAttribute, ELEMENT_NAME));
      document.accept(new TeiNameResolver(ident -> ident.getScore() >= 0.1, Optional.ofNullable(teiAnnotatorName), ELEMENT_NAME));
      return ExportVisitor.export(document);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.printf("## %s%n%n", e.getMessage());
      System.out.println(xml);
      throw e;
    }
  }

  private Gazetteer getGazetteer(Document document) {
    if (globalGazetteer != null) {
      return globalGazetteer;
    } else {
      NamedEntities entities = new NamedEntities();
      document.accept(new TeiNameCollector(entities, elementNames));
      return entities.buildGazetteer().buildMatcher(MatcherType.AHO_CORASICK_MATCHER, 1);
    }
  }

  private Document convertNameTags(Document document) {
    String text = ExportVisitor.export(document);
    text = text.replaceAll(TextAnnotator.BEGIN_OF_NAME, OPEN_TAG);
    text = text.replaceAll(TextAnnotator.END_OF_NAME, CLOSE_TAG);
    return Documents.newDocument(text);
  }

}
