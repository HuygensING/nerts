package nl.knaw.huygens.pergamon.nerts.tool;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import nl.knaw.huygens.pergamon.nerts.Gazetteer;
import nl.knaw.huygens.pergamon.nerts.tei.TeiNameIdentifier;
import nl.knaw.huygens.pergamon.nerts.tei.SimpleNameResolver;
import nl.knaw.huygens.pergamon.support.tei.Attributes;
import nl.knaw.huygens.pergamon.support.tei.Documents;
import nl.knaw.huygens.pergamon.support.tei.export.ExportVisitor;
import nl.knaw.huygens.tei.Document;
import nl.knaw.huygens.tei.Element;

/**
 * Tool for identifying named entities that already have been annotated.
 */
public class TeiIdentifier implements UnaryOperator<String> {

  private final Gazetteer gazetteer;
  private final String[] elementNames;
  private final Predicate<Element> hasProperRespAttribute;
  private Optional<String> teiAnnotatorName;

  public TeiIdentifier(Gazetteer gazetteer, String resp, String... elementNames) {
    this.gazetteer = gazetteer;
    this.elementNames = elementNames;
    hasProperRespAttribute = element -> element.hasAttribute("resp", resp) && !element.hasAttribute(Attributes.KEY);
    teiAnnotatorName = Optional.empty();
  }

  public TeiIdentifier withTeiAnnotatorName(String annotatorName) {
    teiAnnotatorName = Optional.of(annotatorName);
    return this;
  }

  @Override
  public String apply(String xml) {
    try {
      Document document = Documents.newDocument(xml);
      document.accept(new TeiNameIdentifier(gazetteer, hasProperRespAttribute, elementNames));
      document.accept(new SimpleNameResolver(teiAnnotatorName, elementNames));
      return ExportVisitor.export(document);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.printf("## %s%n%n", e.getMessage());
      System.out.println(xml);
      throw e;
    }
  }

}
