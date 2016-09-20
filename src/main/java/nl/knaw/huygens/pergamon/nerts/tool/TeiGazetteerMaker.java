package nl.knaw.huygens.pergamon.nerts.tool;

import java.util.function.UnaryOperator;

import nl.knaw.huygens.pergamon.nerts.NamedEntities;
import nl.knaw.huygens.pergamon.nerts.tei.TeiNameCollector;
import nl.knaw.huygens.pergamon.support.tei.Documents;

/**
 * Adds names marked up in a TEI document to a named entity collection.
 */
public class TeiGazetteerMaker implements UnaryOperator<String> {

  private final NamedEntities entities;
  private final String[] elementNames;

  /**
   * Creates a {@code TeiGazetteerMaker} instance.
   * @param entities the collection to add named entities to.
   * @param elementNames the elements of which the text is added.
   */
  public TeiGazetteerMaker(NamedEntities entities, String... elementNames) {
    this.entities = entities;
    this.elementNames = elementNames;
  }

  @Override
  public String apply(String xml) {
    try {
      Documents.visitXml(xml, new TeiNameCollector(entities, elementNames));
      // Our framework requires implementations to return a string.
      return "";
    } catch (Exception e) {
      System.out.printf("## %s%n%n%s%n", e.getMessage(), xml);
      throw e;
    }
  }

}
