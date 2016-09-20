package nl.knaw.huygens.pergamon.support.tei;

import java.util.function.Predicate;

import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

/**
 * An element handler that controls traversal of the TEI document.
 * Only elements that satisfy the specified predicate are visited.
 */
public class ElementFilter<T extends XmlContext> extends DefaultElementHandler<T> {

  private final Predicate<Element> filter;

  public ElementFilter(Predicate<Element> filter) {
    this.filter = filter;
  }

  @Override
  public Traversal enterElement(Element element, T context) {
    return filter.test(element) ? Traversal.NEXT : Traversal.STOP;
  }

}
