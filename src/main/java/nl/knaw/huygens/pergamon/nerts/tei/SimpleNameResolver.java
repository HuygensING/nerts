package nl.knaw.huygens.pergamon.nerts.tei;

import java.util.Optional;

import nl.knaw.huygens.pergamon.support.tei.Attributes;
import nl.knaw.huygens.pergamon.support.tei.TeiUtils;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

/**
 * Resolves values in the {@code Attributes.KEYS} attribute.
 *
 * If there is just one alternative and its type matches the name of the element:
 * - the key will be transferred to the {@code Attributes.KEY} attribute;
 * - the annotator name is appended to the {@code Attributes.RESP} attribute.
 * The {@code Attributes.KEYS} attribute is always removed.
 */
public class SimpleNameResolver extends DelegatingVisitor<XmlContext> {

  private final Optional<String> annotator;

  public SimpleNameResolver(Optional<String> annotator, String... elementNames) {
    super(new XmlContext());
    this.annotator = annotator;
    addElementHandler(new NameElementHandler(), elementNames);
  }

  private class NameElementHandler extends DefaultElementHandler<XmlContext> {
    @Override
    public Traversal leaveElement(Element element, XmlContext context) {
      if (element.hasAttribute(Attributes.KEYS)) {
        String[] tags = element.getAttribute(Attributes.KEYS).split(" ");
        if (tags.length == 1) {
          String[] fields = tags[0].split("\\|");
          if (element.hasName(fields[0])) {
            element.setAttribute(Attributes.KEY, fields[1]);
            TeiUtils.appendResponsibility(element, annotator);
          }
        }
      }
      element.removeAttribute(Attributes.KEYS);
      return Traversal.NEXT;
    }
  }

};
