package nl.knaw.huygens.pergamon.support.tei;

import java.util.Arrays;
import java.util.List;

import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

/**
 * Assigns default {@code xml:id}'s to specified elements,
 * overriding existing {@code xml:id} values.
 */
public class XmlIdAssigner extends DelegatingVisitor<XmlContext> {

  public XmlIdAssigner(List<String> elementNames) {
    super(new XmlContext());
    for (String elementName : elementNames) {
      // Each element must have its own sequence
      addElementHandler(new XmlIdHandler(), elementName);
    }
  }

  public XmlIdAssigner(String... elementNames) {
    this(Arrays.asList(elementNames));
  }

  private static class XmlIdHandler extends DefaultElementHandler<XmlContext> {
    private int nextNumber = 1;

    @Override
    public Traversal enterElement(Element element, XmlContext context) {
      TeiUtils.setNormalizedXmlIdAttribute(element, nextNumber++);
      return Traversal.NEXT;
    }
  }

}
