package nl.knaw.huygens.pergamon.nerts.tei;

import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

/**
 * Visitor that removes element attributes.
 */
public class AttributeRemover extends DelegatingVisitor<XmlContext> {

  public AttributeRemover(String key) {
    super(new XmlContext());
    setDefaultElementHandler(new AttributeHandler(key));
  }

  private static class AttributeHandler extends DefaultElementHandler<XmlContext> {
    private final String key;

    public AttributeHandler(String key) {
      this.key = key;
    }

    @Override
    public Traversal enterElement(Element element, XmlContext context) {
      element.removeAttribute(key);
      return Traversal.NEXT;
    }
  }

}
