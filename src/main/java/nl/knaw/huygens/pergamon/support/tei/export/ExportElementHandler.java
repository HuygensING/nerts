package nl.knaw.huygens.pergamon.support.tei.export;

import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.ElementHandler;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;

import java.util.HashMap;
import java.util.Map;

public class ExportElementHandler implements ElementHandler<XmlContext> {

  private final Map<String, AttributeFilter> filters;

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
    AttributeFilter filter = filters.get(element.getName());
    return (filter != null) ? filter.apply(element) : element;
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
