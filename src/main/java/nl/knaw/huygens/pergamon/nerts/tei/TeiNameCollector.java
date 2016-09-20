package nl.knaw.huygens.pergamon.nerts.tei;

import nl.knaw.huygens.pergamon.nerts.NamedEntities;
import nl.knaw.huygens.pergamon.support.tei.ElementTextHandler;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.XmlContext;

/**
 * Extracts the text of specified TEI elements and adds them to a named entity collection.
 */
public class TeiNameCollector extends DelegatingVisitor<XmlContext> {

  private static final String KEY_ATTRIBUTE = "key";

  /**
   * Constructs a {@code TeiNameCollector2} instance.
   * @param entities the collection to add named entities to.
   * @param elementNames the elements of which the text is added.
   */
  public TeiNameCollector(final NamedEntities entities, String... elementNames) {
    super(new XmlContext());
    addElementHandler(new ElementTextHandler<XmlContext>() {
      @Override
      public void handleText(Element element, String text) {
        entities.add(text, element.getName(), element.getAttribute(KEY_ATTRIBUTE), 1);
      }
    }, elementNames);
  }

};
