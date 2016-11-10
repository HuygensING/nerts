package nl.knaw.huygens.pergamon.support.tei.export;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.ElementHandler;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;

public class ExportElementHandler implements ElementHandler<XmlContext> {

  private static final UnaryOperator<Element> IDENTITY = UnaryOperator.identity();

  private final Map<String, UnaryOperator<Element>> filters;

  public ExportElementHandler() {
    filters = new HashMap<>();
  }

  public ExportElementHandler addAttributeFilter(AttributeFilter filter, String... elementNames) {
    for (String elementName : elementNames) {
      filters.put(elementName, filter);
    }
    return this;
  }

  private Element filterAttributes(Element element) {
    return filters.getOrDefault(element.getName(), IDENTITY).apply(element);
  }

  @Override
  public Traversal enterElement(Element element, XmlContext context) {
    if (element.hasChildren()) {
      context.addOpenTag(filterAttributes(element));
    } else {
      context.addEmptyElementTag(filterAttributes(element));
    }
    return Traversal.NEXT;
  }

  @Override
  public Traversal leaveElement(Element element, XmlContext context) {
    if (element.hasChildren()) {
      context.addCloseTag(element);
    }
    return Traversal.NEXT;
  }

}
